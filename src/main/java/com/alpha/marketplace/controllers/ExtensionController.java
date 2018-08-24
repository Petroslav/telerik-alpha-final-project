package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/extension")
public class ExtensionController {

    private UserService userService;
    private ExtensionService extensionService;

    @Autowired
    public ExtensionController(UserService userService, ExtensionService extensionService) {
        this.extensionService = extensionService;
        this.userService = userService;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        List<Extension> extensions = extensionService.getAllApproved();

        model.addAttribute("view", "extensions/all");
        model.addAttribute("extensions", extensions);

        return "base-layout";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        if (Utils.isUserNotAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("view", "extensions/create");
        return "base-layout";
    }
    @PostMapping("/create")
    public String create(ExtensionBindingModel model){
        extensionService.createExtension(model);

        return "redirect:/";
    }
    @GetMapping("/{id}")
    public String viewExtension(@PathVariable String id, Model model){

        int intId = Integer.parseInt(id);
        Extension extension = extensionService.getById(intId);
        if(extension == null){
            model.addAttribute("view", "error/404");
            return "base-layout";
        }
        String approved = "isNotApproved";

        if(extension.isApproved()){
            approved = "Approved";
        }


        model.addAttribute("view", "extensions/details");
        model.addAttribute("approved", approved);
        model.addAttribute("extension", extension);
        return "base-layout";
    }
}
