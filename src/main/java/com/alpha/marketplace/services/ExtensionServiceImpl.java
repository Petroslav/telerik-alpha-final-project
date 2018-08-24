package com.alpha.marketplace.services;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.repositories.base.*;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.utils.Utils;
import com.google.cloud.storage.BlobId;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ExtensionRepository repository;
    private UserRepository userRepository;
    private TagRepository tagRepository;
    private CloudExtensionRepository cloudExtensionRepository;
    private final ModelMapper mapper;
    private GitHubRepository gitHubRepository;

    @Autowired
    public ExtensionServiceImpl(ExtensionRepository repository,
                                UserRepository userRepository,
                                CloudExtensionRepository cloudExtensionRepository,
                                TagRepository tagRepository,
                                ModelMapper mapper,
                                GitHubRepository gitHubRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.cloudExtensionRepository = cloudExtensionRepository;
        this.tagRepository = tagRepository;
        this.mapper = mapper;
        this.gitHubRepository = gitHubRepository;
    }


    @Override
    public List<Extension> getMostPopular() {
        return getAllApproved().stream()
                .sorted(Comparator.comparing(Extension::getDownloads).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Extension> getAdminSelection() {
        //TODO implement admin selection
        return null;
    }

    @Override
    public List<Extension> getLatest() {
        return getAllApproved().stream()
                .sorted(Comparator.comparing(Extension::getAddedOn).reversed())
                .limit(10)
                .collect(Collectors.toList());
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
        //TODO FIX PEASANT STYLE CODE
        repository.save(extension);
        extension.setGitHubInfo(new GitHubInfo());
        extension.getGitHubInfo().setParent(extension);
        Utils.setGithubInfo(extension.getGitHubInfo());
        gitHubRepository.save(extension.getGitHubInfo());
        repository.update(extension);
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
        return repository.getAll().stream()
                .filter(Extension::isApproved).collect(Collectors.toList());
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

}
