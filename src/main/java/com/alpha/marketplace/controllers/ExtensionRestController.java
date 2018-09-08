package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.models.edit.ExtensionEditModel;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/extension")
public class ExtensionRestController {

    private ExtensionService extensionService;
    private UserService userService;

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
        return new HashSet<>(extensionService.getAllApproved());
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Extension createExtension(@RequestParam("key") String key, @ModelAttribute ExtensionBindingModel model, BindingResult errors) {
        if(errors.hasErrors()){
            return null;
        }
        long id = Long.parseLong(Utils.getIdStringFromToken(key));
        User u = userService.findByIdFromMemory(id);
        extensionService.createExtension(model, errors, u);
        Extension ex = null;
        return ex;
    }
}
