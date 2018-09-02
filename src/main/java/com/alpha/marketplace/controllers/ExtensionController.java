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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        if (Utils.userIsAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("view", "extensions/create");
        return "base-layout";
    }

    @PostMapping("/create")
    public String create(ExtensionBindingModel model, BindingResult errors) {
        extensionService.createExtension(model, errors);

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
        if (!Utils.userIsAnonymous()) {
            User user = extensionService.currentUser();
            model.addAttribute("user", user);

        }
        String approved = "isNotApproved";
        if (extension.isApproved()) {
            approved = "Approved";
        }
        //TODO remove after tags are changed to Set

        model.addAttribute("view", "extensions/details");
        model.addAttribute("approved", approved);
        model.addAttribute("tags", extension.getTags());
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
            return "redirect:/extension/" + id;
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
            return "redirect:/extension/" + id;
        }
        extensionService.delete(id);

        return "redirect:/";
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveExtension(Model model, @PathVariable("id") Integer id) {


        extensionService.approveExtensionById(id);

        extensionService.reloadLists();

        return "redirect:/extension/" + id;
    }

    @PostMapping("/download/{id}")
    @PreAuthorize("isAuthenticated()")
    public String download(@PathVariable("id") Integer id) {

        Extension extension = extensionService.getById(id);
        extensionService.download(id);

        return "redirect:"+extension.getDlURI();
    }

    @PostMapping("/feature/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String featureExtension( @PathVariable("id") Integer id) {
        if(extensionService.getAdminSelection().size() == 10){
            return "redirect:/featuredSelection";
        }
        extensionService.setFeatured(id);
        extensionService.reloadLists();

        return "redirect:/extension/" + id;
    }
    @PostMapping("/unFeature/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String unFeatureExtension( @PathVariable("id") Integer id) {
        extensionService.removeFeatured(id);
        extensionService.reloadLists();

        return "redirect:/extension/" + id;
    }

    @GetMapping("/featuredSelection")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getFeaturedSelection(Model model){
        model.addAttribute("view", "/admin/featuredSelection");
        model.addAttribute("featured", extensionService.getAdminSelection());
        return "base-layout";
    }

    @PostMapping("/removeFeatured")
    public String removeFeatured(Model model, @RequestParam("list") List<Long> stuff){
        stuff.forEach(id -> extensionService.removeFeatured(id));
        extensionService.reloadLists();

        return "redirect:/admin";
    }


}
