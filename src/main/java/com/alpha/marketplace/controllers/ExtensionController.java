package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String create(ExtensionBindingModel model) {
        extensionService.createExtension(model);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String viewExtension(@PathVariable String id, Model model) {

        int intId = Integer.parseInt(id);
        Extension extension = extensionService.getById(intId);
        if (extension == null) {
            model.addAttribute("view", "error/404");
            return "base-layout";
        }
        if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
            User user = extensionService.currentUser();
            model.addAttribute("user", user);

        }
        String approved = "isNotApproved";
        if (extension.isApproved()) {
            approved = "Approved";
        }


        model.addAttribute("view", "extensions/details");
        model.addAttribute("approved", approved);
        model.addAttribute("extension", extension);
        return "base-layout";
    }

    @PostMapping("/sync/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String syncExtension(Model model, @PathVariable("id") String id) {
        extensionService.sync(Integer.parseInt(id));

        return "redirect:/extension/" + id;
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deletePage(Model model, @PathVariable("id") Integer id) {
        Extension extension = extensionService.getById(id);

        if (!extensionService.isUserPublisherOrAdmin(extension)) {
            return "redirect :/extension/" + id;
        }
        model.addAttribute("view", "/extensions/delete");
        model.addAttribute("extension", extension);

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("isAuthenticated()")
    public String deleteExtension(Model model, @PathVariable("id") Integer id) {

        Extension extension = extensionService.getById(id);
        if (!extensionService.isUserPublisherOrAdmin(extension)) {
            return "redirect :/extension/" + id;
        }
        extensionService.delete(id);

        return "redirect :/";
    }
}
