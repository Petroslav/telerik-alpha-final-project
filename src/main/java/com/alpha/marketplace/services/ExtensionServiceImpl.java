package com.alpha.marketplace.services;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.repositories.base.ExtensionRepository;
import com.alpha.marketplace.repositories.base.UserRepository;
import com.alpha.marketplace.services.base.ExtensionService;
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

    @Autowired
    public ExtensionServiceImpl(ExtensionRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
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
                .sorted(Comparator.comparing(Extension::getAddedOn))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public void createExtension(ExtensionBindingModel model) {
        //TODO implement validation

        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User u = userRepository.findByUsername(user.getUsername());
        Extension extension = new Extension();
        extension.setName(model.getName());
        extension.setDescription(model.getDescription());
        extension.setApproved(true);
        extension.setDownloads(0);
        extension.setTags(new ArrayList<>());
        extension.setAddedOn(new Date());
        extension.setVersion("1");
         extension.setPublisher(u);
        //TODO get current logged user to set as publisher
        extension.setDlURI(model.getDownloadLink());
        extension.setRepoURL(model.getRepositoryUrl());

        repository.save(extension);
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

}
