package com.alpha.marketplace.services;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.repositories.base.*;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.utils.Utils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExtensionServiceImpl implements ExtensionService {

    private final ExtensionRepository repository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CloudExtensionRepository cloudExtensionRepository;
    private final ModelMapper mapper;
    private final GitHubRepository gitHubRepository;
    private List<Extension> all;
    private List<Extension> approved;
    private List<Extension> latest;
    private List<Extension> mostPopular;

    @Autowired
    public ExtensionServiceImpl(
            ExtensionRepository repository,
            UserRepository userRepository,
            CloudExtensionRepository cloudExtensionRepository,
            TagRepository tagRepository,
            ModelMapper mapper,
            GitHubRepository gitHubRepository
    ) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cloudExtensionRepository = cloudExtensionRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.gitHubRepository = gitHubRepository;
        this.all = repository.getAll();
        this.approved = new ArrayList<>();
        this.latest = new ArrayList<>();
        this.mostPopular = new ArrayList<>();
    }


    @Override
    public List<Extension> getMostPopular() {
        if (mostPopular.isEmpty() || mostPopular == null) {
            mostPopular = approved.stream()
                    .sorted(Comparator.comparing(Extension::getDownloads).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return mostPopular;
    }

    @Override
    public List<Extension> getAdminSelection() {
        //TODO implement admin selection
        return null;
    }

    @Override
    public List<Extension> getLatest() {
        if (latest.isEmpty() || latest == null) {
            latest = approved.stream()
                    .sorted(Comparator.comparing(Extension::getAddedOn).reversed())
                    .limit(10)
                    .collect(Collectors.toList());
        }
        return latest;
    }

    @Override
    public void createExtension(ExtensionBindingModel model) {
        //TODO implement validation

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        //Added cloud storage example to code -- to be refactored
        User u = userRepository.findByUsername(user.getUsername());
        Extension extension = mapper.map(model, Extension.class);
        extension.setName(model.getName());
        extension.setDescription(model.getDescription());
        extension.setApproved(true);
        extension.setDownloads(0);
        extension.setTags(new ArrayList<>());
        extension.setAddedOn(new Date());
        extension.setVersion("1");
        extension.setPublisher(u);
//        BlobId blobid = cloudExtensionRepository.saveExtension("1", extension.getName(), "contentType", new byte[24]);
//        extension.setBlobId(blobid);
//        String extensionURI = cloudExtensionRepository.getEXTENSION_URL_PREFIX() + blobid.getName();
//        extension.setDlURI(extensionURI);
        //TODO get current logged user to set as publisherqqq
        extension.setDlURI(model.getDownloadLink());
        extension.setRepoURL(model.getRepositoryUrl());
        repository.save(extension);
        extension.setGitHubInfo(new GitHubInfo());
        extension.getGitHubInfo().setParent(extension);
        Utils.setGithubInfo(extension.getGitHubInfo());
        gitHubRepository.save(extension.getGitHubInfo());
        repository.update(extension);
        reloadLists();
    }

    @Override
    public Extension getById(int id) {
        if (id < 0) {
            //TODO error handling
            return null;
        }
        return repository.getById(id);
    }

    @Override
    public List<Extension> getAllApproved() {
        if (approved.isEmpty() || approved == null) {
            approved =  all.stream()
                    .filter(Extension::isApproved).collect(Collectors.toList());
        }
        return approved;
    }

    @Override
    public void approveExtensionById(int id) {
        Extension extension = getById(id);

        extension.approve();
    }

    @Override
    public Extension getByName(String name) {
        return repository.getByName(name);
    }


    //TODO add logging for github sync
    @Override
    public void sync(int id) {
        Extension extension = getById(id);
        GitHubInfo info = extension.getGitHubInfo();
        Date currentTime = new Date();
        System.out.println("[" + currentTime + "]" + "Admin syncing for" + extension.getName() + ":");
        Utils.setGithubInfo(info);
        gitHubRepository.update(info);
        System.out.println("--Updated info for " + extension.getName());
    }

    //Current sync set at 2 hours
    @Scheduled(fixedRate = 2 * 60 * 60 * 1000)
    public void syncAll() {
        Date currentTime = new Date();
        List<Extension> extensions = getAllApproved();
        System.out.println("[" + currentTime + "]" + "Syncing:");
        for (Extension e : extensions) {
            if (e.getGitHubInfo() == null) {
                continue;
            }
            GitHubInfo ginfo = e.getGitHubInfo();
            Utils.setGithubInfo(ginfo);
            gitHubRepository.update(ginfo);
            System.out.println("--Updated info for " + e.getName());
        }
        reloadLists();
    }

    private void reloadLists() {
        all.clear();
        approved.clear();
        mostPopular.clear();
        latest.clear();
        all = repository.getAll();
        approved = getAllApproved();
        mostPopular = getMostPopular();
        latest = getLatest();
        System.out.println("Lists reloaded");
    }

}
