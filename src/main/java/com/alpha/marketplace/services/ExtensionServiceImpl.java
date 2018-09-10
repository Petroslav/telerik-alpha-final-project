package com.alpha.marketplace.services;

import com.alpha.marketplace.exceptions.ErrorMessages;
import com.alpha.marketplace.exceptions.FailedToSyncException;
import com.alpha.marketplace.exceptions.VersionMismatchException;
import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.Properties;
import com.alpha.marketplace.models.Tag;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.models.edit.ExtensionEditModel;
import com.alpha.marketplace.repositories.base.*;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.Blob;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ExtensionServiceImpl implements ExtensionService {

    private final ExtensionRepository repository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CloudExtensionRepository cloudExtensionRepository;
    private final GitHubRepository gitHubRepository;
    private final PropertiesRepository propertiesRepository;
    private final ModelMapper mapper;

    private Map<Long, Extension> extensionsMap;
    private List<Extension> all;
    private List<Extension> approved;
    private List<Extension> latest;
    private List<Extension> mostPopular;
    private List<Extension> unApproved;
    private List<Extension> selected;

    private ExecutorService workers;
    private ScheduledExecutorService syncWorker;
    private ScheduledFuture syncManager;
    private long delay;

    @Autowired
    public ExtensionServiceImpl(
            ExtensionRepository repository,
            UserRepository userRepository,
            CloudExtensionRepository cloudExtensionRepository,
            TagRepository tagRepository,
            ModelMapper mapper,
            GitHubRepository gitHubRepository,
            PropertiesRepository propertiesRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cloudExtensionRepository = cloudExtensionRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.gitHubRepository = gitHubRepository;
        this.extensionsMap = new HashMap<>();
        this.all = new ArrayList<>();
        this.approved = new ArrayList<>();
        this.latest = new ArrayList<>();
        this.mostPopular = new ArrayList<>();
        this.unApproved = new ArrayList<>();
        this.selected = new ArrayList<>();
        this.propertiesRepository = propertiesRepository;
        workers = Executors.newFixedThreadPool(5);
        syncWorker = Executors.newScheduledThreadPool(1);
    }


    @Override
    public List<Extension> getAll() {
        if(all.isEmpty()) {
            all = repository.getAll();
            extensionsMap.clear();
            all.forEach(ex -> extensionsMap.put(ex.getId(), ex));
        }
        return all;
    }

    @Override
    public List<Extension> getMostPopular() {
        if (mostPopular.isEmpty()) {
            mostPopular = getAllApproved().stream()
                    .sorted(Comparator.comparing(Extension::getDownloads).reversed())
                    .collect(Collectors.toList());
        }
        return mostPopular;
    }

    @Override
    public List<Extension> getAdminSelection() {
        if (selected.isEmpty()) {
            selected = getAllApproved().stream()
                    .filter(Extension::isSelected)
                    .sorted(Comparator.comparing(Extension::getSelectionDate).reversed())
                    .limit(5)
                    .collect(Collectors.toList());
        }
        return selected;
    }

    @Override
    public List<Extension> getLatest() {
        if (latest.isEmpty()) {
            latest = getAllApproved().stream()
                    .sorted(Comparator.comparing(Extension::getAddedOn).reversed())
                    .collect(Collectors.toList());
        }
        return latest;
    }

    @Override
    public List<Extension> getAllApproved() {
        if (approved.isEmpty()) {
            approved =  getAll().stream()
                    .filter(Extension::isApproved)
                    .filter(extension -> !extension.getPublisher().isBanned()).collect(Collectors.toList());
        }
        return approved;
    }

    @Override
    public List<Extension> getUnapproved() {
        if (unApproved.isEmpty()) {
            unApproved =  getAll().stream()
                    .filter(extension -> !extension.isApproved()).collect(Collectors.toList());
        }
        return unApproved;
    }

    @Override
    public Set<Extension> searchExtensions(String criteria) {
        return new HashSet<>(repository.search(criteria));
    }

    @Override
    public Set<Extension> searchExtensionsByTag(String criteria) {
        Set<Extension> matches = new HashSet<>();
        tagRepository.search(criteria).forEach(tag -> matches.addAll(tag.getTaggedExtensions()));
        return matches;
    }

    @Override
    public Extension getById(long id) {
        if (id < 0) {
            return null;
        }
        return repository.getById(id);
    }

    @Override
    public Extension getByIdFromMemory(long id) {
        if(id < 0){
            return null;
        }
        return extensionsMap.get(id);
    }

    @Override
    public Extension getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public User currentUser() {
        if(Utils.userIsAnonymous()){
            return null;
        }
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return userRepository.findByUsername(user.getUsername());
    }

    @Override
    public boolean isUserPublisherOrAdmin(Extension extension) {
        if(Utils.userIsAnonymous()){
            return false;
        }
        User u = currentUser();
        return u.isAdmin() || u.isPublisher(extension);
    }

    @Override
    public boolean update(Extension extension) {
        try {
            repository.update(extension);
            extensionsMap.put(extension.getId(), extension);
        } catch (VersionMismatchException e) {
            System.out.println("Version mismatch. Please retry the operation for extension " + extension.getName());
            return false;
        }
        return true;
    }

    @Override
    public boolean edit(ExtensionEditModel model, long id, UserService userService) {
        Extension edit = extensionsMap.get(id);
        edit.setName(model.getName());
        edit.setDescription(model.getDescription());
        edit.setVersion(model.getVersion());
        edit.setRepoURL(model.getRepo());
        edit.setLatestUpdate(new Date());

        if(!model.getFile().isEmpty()){
            if(!updateExtensionFiles(edit, model)) return false;
        }
        if(!model.getPicture().isEmpty()) {
            if(!updateExtensionPicture(edit, model)) return false;
        }

        Set<Tag> tags = handleTags(model.getTags(), edit);
        edit.getTags().clear();
        edit.getTags().addAll(tags);
        if(!update(edit)){
            return false;
        }
        workers.submit(this::reloadLists);
        workers.submit(userService::reloadMemory);
        return true;
    }

    @Override
    public void unfeatureList(List<Long> extensions) {
        List<Extension> matches = new ArrayList<>();
        extensions.forEach(ex -> matches.add(
                getAdminSelection()
                        .stream()
                        .filter(e1 -> ex == e1.getId())
                        .collect(Collectors.toList())
                        .get(0)));
        matches.forEach(e -> e.setSelected(false));
        workers.submit(() -> matches.forEach(this::update));
    }

    @Override
    public void approveList(List<Long> ids) {
        List<Extension> matches = new ArrayList<>();
        ids.forEach(ex -> matches.add(
                getUnapproved()
                        .stream()
                        .filter(e1 -> ex == e1.getId())
                        .collect(Collectors.toList())
                        .get(0)));
        matches.forEach(Extension::approve);
        matches.forEach(e -> extensionsMap.get(e.getId()).approve());
        workers.submit(() -> {
            matches.forEach(this::update);
            reloadLists();
        });
    }

    @Override
    public void createExtension(ExtensionBindingModel model, BindingResult errors, User user, UserService userService) {
        if(errors.hasErrors()) return;

        if(!validateRepoUrl(model.getRepositoryUrl())){
            errors.addError(new ObjectError("link", ErrorMessages.BAD_REPOSITORY));
            return;
        }

        if(model.getFile().isEmpty() || model.getPic().isEmpty()){
            errors.addError(new ObjectError("noFile", ErrorMessages.NO_FILE));
            return;
        }

        Extension extension = mapper.map(model, Extension.class);
        extension.setPublisher(user);
        extension.setRepoURL(model.getRepositoryUrl());
        finishCreatingExtension(extension, model, errors, userService);
    }

    @Override
    public Extension createExtension(User publisher, ExtensionBindingModel model, BindingResult errors, UserService userService) {
        if(errors.hasErrors()) return null;

        if(!validateRepoUrl(model.getRepositoryUrl())){
            errors.addError(new ObjectError("link", ErrorMessages.BAD_REPOSITORY));
            return null;
        }

        if(model.getFile().isEmpty() || model.getPic().isEmpty()){
            errors.addError(new ObjectError("noFile", ErrorMessages.NO_FILE));
            return null;
        }

        Extension extension = mapper.map(model, Extension.class);
        extension.setPublisher(publisher);
        extension.setRepoURL(model.getRepositoryUrl());
        finishCreatingExtension(extension, model, errors, userService);

        return extension;
    }

    private void finishCreatingExtension(Extension extension, ExtensionBindingModel model, BindingResult errors, UserService userService){
        try {
            storeFiles(extension, model);
        }catch(IOException e){
            System.out.println(e.getMessage());
            if(extension.getBlobId() != null){
                cloudExtensionRepository.delete(extension.getBlobId());
            }
            errors.addError(new ObjectError("file", ErrorMessages.FILE_UPLOAD_FAIL));
            return;
        }

        if(repository.save(extension)){
            extension.getPublisher().getExtensions().add(extension);
            extensionsMap.put(extension.getId(), extension);
        }

        workers.submit(() -> {
            try {
                saveExtensionGitInfo(extension, model, userService);
            } catch (FailedToSyncException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @Override
    public void delete(long id, UserService userService) {
        repository.delete(id);
        reloadLists();
        userService.reloadMemory();
    }

    @Override
    public void approveExtensionById(long id, UserService userService) {
        extensionsMap.get(id).approve();
        workers.submit(() -> {
            while(true){
                try{
                    Extension extension = getById(id);
                    extension.approve();
                    repository.update(extension);
                    reloadLists();
                    userService.reloadMemory();
                    break;
                }catch(VersionMismatchException e){
                    System.out.println(e.getMessage());
                    System.out.println("Retrying...");
                }
            }});
    }

    @Override
    public void disapproveExtensionById(long id, UserService userService) {
        extensionsMap.get(id).disapprove();
        workers.submit(() -> {
            while(true){
                try{
                    Extension extension = getById(id);
                    extension.disapprove();
                    repository.update(extension);
                    reloadLists();
                    userService.reloadMemory();
                    break;
                }catch(VersionMismatchException e){
                    System.out.println(e.getMessage());
                    System.out.println("Retrying...");
                }
            }});

    }

    @Override
    public void reloadLists() {
        all.clear();
        approved.clear();
        mostPopular.clear();
        latest.clear();
        unApproved.clear();
        selected.clear();
        all = repository.getAll();
        approved = getAllApproved();
        mostPopular = getMostPopular();
        latest = getLatest();
        unApproved = getUnapproved();
        selected = getAdminSelection();
        System.out.println("Lists reloaded");
    }

    @Override
    public void sync(long id) {
        Extension extension = getById(id);
        GitHubInfo info = extension.getGitHubInfo();
        Date currentTime = new Date();
        System.out.println("[" + currentTime + "]" + "Admin syncing for " + extension.getName() + ":");
        Properties properties = Utils.properties;
        try {
            Utils.updateGithubInfo(info);
            gitHubRepository.update(info);
            System.out.println("--Updated info for " + extension.getName());
            properties.setLastSuccessfulSync(new Date());
        } catch (FailedToSyncException e) {
            properties.setLastFailedSync(new Date());
        }

        propertiesRepository.update();
    }

    @Override
    public void setSync(long period){

        if(syncManager != null) syncManager.cancel(true);
        Properties properties = Utils.properties;
        if(delay != period){
            properties.setDelay(period);
            propertiesRepository.update();
        }
        syncManager = syncWorker.scheduleWithFixedDelay(this::syncAll, 0L, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void download(long id) {
        workers.submit(() -> dlUpdate(id));
    }

    @Override
    public void setFeatured(long id) {
        Extension extension = getAll().stream()
                .filter(ex -> ex.getId() == id)
                .collect(Collectors.toList())
                .get(0);
        if(extension.isSelected()){
            return;
        }
        extension.setSelected(true);
        extension.setSelectionDate(new Date());
        if(update(extension)){
            reloadLists();
        }
    }

    @Override
    public void removeFeatured(long id) {
        Extension extension = getAll().stream()
                .filter(ex -> ex.getId() == id)
                .collect(Collectors.toList())
                .get(0);
        if(!extension.isSelected()){
            return;
        }
        extension.setSelected(false);
        if(update(extension)){
            reloadLists();
        }

    }

    @PostConstruct
    public void initializeSync(){
        Properties properties = Utils.properties;
        delay = properties.getDelay();
        setSync(delay);
    }

    private boolean validateRepoUrl(String repo){
        String GITHUB_PREFIX = "https://github.com/";
        if(!repo.startsWith(GITHUB_PREFIX)){
            return false;
        }
        repo = repo.substring(Utils.GITHUB_URL_PREFIX.length());
        String[] words = repo.split("/");
        return words.length >= 2;
    }

    @Override
    public void syncAllExtensions(){
        workers.submit(this::syncAll);
    }

    private void syncAll() {
        Date currentTime = new Date();
        List<Extension> extensions = getAllApproved();
        System.out.println("[" + currentTime + "]" + "Syncing:");
        Properties properties = Utils.properties;
        boolean failed = false;
        for (Extension e : extensions) {
            if (e.getGitHubInfo() == null) {
                continue;
            }
            GitHubInfo gitHubInfo = e.getGitHubInfo();

            try {
                Utils.updateGithubInfo(gitHubInfo);
                gitHubRepository.update(gitHubInfo);
                System.out.println("--Updated info for " + e.getName());
            } catch (FailedToSyncException e1) {
                failed = true;
                properties.setLastFailedSync(new Date());
                properties.setFailInfo("Could not finish synchronization. " + e1.getMessage());
            }
        }

        if(!failed){
            properties.setLastSuccessfulSync(new Date());
        }

        propertiesRepository.update();
        reloadLists();
    }

    private Set<Tag> handleTags(String tagString, Extension extension){
        String [] tagArray = tagString.split(", ");
        Set<Tag> tags = new HashSet<>();

        for (String tag: tagArray) {
            Tag t = tagRepository.findByName(tag);

            if(t == null){
                Tag newTag = new Tag(tag);
                tagRepository.saveTag(newTag);
                tags.add(newTag);
            }
            else {
                t.getTaggedExtensions().add(extension);

                tags.add(t);
                tagRepository.updateTag(t);
            }
        }
        return tags;
    }


    private void storeFiles(Extension extension, ExtensionBindingModel model) throws IOException {
        //DECLARE VARIABLES AND GET THE FILE EXTENSION
        Blob blob;
        String fileext = model.getFile().getOriginalFilename();
        String picext = model.getPic().getOriginalFilename();
        int indexFile = fileext.lastIndexOf(".");
        int indexPic = picext.lastIndexOf(".");

        if(indexPic == -1 || indexFile == -1) return;

        fileext = fileext.substring(indexFile);
        picext = picext.substring(indexPic);

        //CALL SERVICE TO SAVE FILE
        blob = cloudExtensionRepository.saveExtension(
                String.valueOf(extension.getPublisher().getId()),
                extension.getName() + fileext,
                model.getFile().getContentType(),
                model.getFile().getBytes()
        );

        extension.setBlobId(blob.getBlobId());
        extension.setDlURI(blob.getMediaLink());
        //REPEAT FOR PIC
        Blob picBlob = cloudExtensionRepository.saveExtensionPic(
                String.valueOf(extension.getPublisher().getId()),
                extension.getName() + picext,
                model.getPic().getContentType(),
                model.getPic().getBytes()
        );

        extension.setPicBlobId(picBlob.getBlobId());
        extension.setPicURI(picBlob.getMediaLink());
    }

    private void saveExtensionGitInfo(Extension extension, ExtensionBindingModel model, UserService userService) throws FailedToSyncException {
        GitHubInfo info = new GitHubInfo();
        extension.setGitHubInfo(info);
        info.setParent(extension);
        Utils.updateGithubInfo(extension.getGitHubInfo());
        gitHubRepository.save(extension.getGitHubInfo());
        Set<Tag> tags = handleTags(model.getTagString(), extension);
        extension.setTags(tags);
        try {
            repository.update(extension);
            System.out.println("GitHub information successfully updated for extension " + extension.getName());
            System.out.println("Reloading lists...");
            reloadLists();
            userService.reloadMemory();
        } catch (VersionMismatchException e) {
            System.out.println("Version mismatch. Retrying...");
            extension = repository.getById(extension.getId());
            saveExtensionGitInfoRetry(extension, info, tags);
        }
    }

    private void saveExtensionGitInfoRetry(Extension extension, GitHubInfo info, Set<Tag> tags) {
        extension.setGitHubInfo(info);
        extension.setTags(tags);
        try {
            repository.update(extension);
            System.out.println("GitHub information successfully updated for extension " + extension.getName());
            System.out.println("Reloading lists...");
            reloadLists();
        } catch (VersionMismatchException e) {
            System.out.println("Version mismatch. Retrying again in 5 seconds...");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                System.out.println("Something went horribly wrong trying to update extension " + extension.getName());
                System.out.println("Attempting to retry...");
            }finally {
                extension = repository.getById(extension.getId());
                saveExtensionGitInfoRetry(extension, info, tags);
            }
        }
    }

    private void dlUpdate(long id){
        try{
            Extension extension = getById(id);
            extension.setDownloads(extension.getDownloads()+1);
            repository.update(extension);
            reloadLists();
            System.out.println("Extension "+extension.getName()+" downloaded successfully");
            System.out.println("number of downloads: "+extension.getDownloads());
        }catch (VersionMismatchException e) {
            System.out.println("Version mismatch. Retrying update in 5 seconds.");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e1) {
                System.out.println("Something went horribly wrong trying to update the downloads of the extension with ID " + id);
            }finally {
                dlUpdate(id);
            }
        }
    }

    private boolean updateExtensionPicture(Extension edit, ExtensionEditModel model) {
        if(edit.getPicBlobId() != null){
            cloudExtensionRepository.delete(edit.getBlobId());
        }
        try {
            String fn = model.getPicture().getOriginalFilename().substring(model.getPicture().getOriginalFilename().lastIndexOf("."));
            String id = String.valueOf(edit.getPublisher().getId());
            String name = edit.getName() + fn;
            String ct = model.getPicture().getContentType();
            byte[] bytes = model.getPicture().getBytes();
            Blob b =cloudExtensionRepository.saveExtensionPic(id, name, ct, bytes);
            edit.setPicBlobId(b.getBlobId());
            edit.setPicURI(b.getMediaLink());
        } catch (IOException e) {
            System.out.println("Something went wrong while uploading the file.");
            return false;
        }

        return true;
    }

    private boolean updateExtensionFiles(Extension edit, ExtensionEditModel model) {
        if(edit.getBlobId() != null){
            cloudExtensionRepository.delete(edit.getBlobId());
        }
        try {
            String fn = model.getFile().getOriginalFilename().substring(model.getFile().getOriginalFilename().lastIndexOf("."));
            String id = String.valueOf(edit.getPublisher().getId());
            String name = edit.getId() + fn;
            String ct = model.getFile().getContentType();
            byte[] bytes = model.getFile().getBytes();
            Blob b = cloudExtensionRepository.saveExtension(id, name, ct, bytes);
            edit.setBlobId(b.getBlobId());
            edit.setDlURI(b.getMediaLink());
        } catch (IOException e) {
            System.out.println("Something went wrong while uploading the file.");
            return false;
        }

        return true;
    }

}
