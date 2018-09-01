package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.services.base.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/extension")
public class ExtensionRestController {

    private ExtensionService extensionService;

    @Autowired
    public ExtensionRestController(ExtensionService extensionService) {
        this.extensionService = extensionService;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Extension getById(@PathVariable("id") long id) {
        return extensionService.getById(id);
    }

    @RequestMapping(value = "/approved", method = RequestMethod.GET)
    public Set<Extension> getAllApproved() {

        Set<Extension> set = new HashSet<>(extensionService.getAllApproved());
        System.out.println("I WEIGH THIS MUCH: " + set.size());
        return set;
    }

    @RequestMapping(value = "/mostPopular", method = RequestMethod.GET)
    public Set<Extension> getMostPopular() {
        return new HashSet<>(extensionService.getMostPopular());
    }

    @RequestMapping(value = "/featured", method = RequestMethod.GET)
    public Set<Extension> getFeatured() {
        return new HashSet<>(extensionService.getAdminSelection());
    }

    @RequestMapping(value = "/newest", method = RequestMethod.GET)
    public Set<Extension> getNewest() {
        return new HashSet<>(extensionService.getAllApproved());
    }
}
