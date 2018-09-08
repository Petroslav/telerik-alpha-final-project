package com.alpha.marketplace.controllers;

import com.alpha.marketplace.exceptions.ErrorMessages;
import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.Tag;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.models.edit.ExtensionEditModel;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
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
    @PreAuthorize("hasRole('USER')")
    public String createPage(Model model) {
        model.addAttribute("view", "extensions/create");
        return "base-layout";
    }

    @PostMapping("/create")
    public String create(ExtensionBindingModel model, BindingResult errors) {
        User user =  extensionService.currentUser();
        extensionService.createExtension(model, errors, user);
        if (errors.hasErrors()) {
            return "redirect:/extension/create";
        }
        userService.updateUser(user);
        userService.reloadMemory();
        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String viewExtension(@PathVariable("id") long id, Model model) {

        Extension extension = extensionService.getByIdFromMemory(id);
        if (extension == null) {
            return "redirect:/";
        }
        if (!extension.isApproved()) {
            if (!extensionService.isUserPublisherOrAdmin(extension)) {
                return "redirect:/";
            }
        }

        if (!Utils.userIsAnonymous()) {
            User user = extensionService.currentUser();
            model.addAttribute("user", user);

        }
        String approved = "isNotApproved";
        if (extension.isApproved()) {
            approved = "Approved";
        }

        model.addAttribute("view", "extensions/details");
        model.addAttribute("approved", approved);
        model.addAttribute("tags", extension.getTags());
        model.addAttribute("extension", extension);

        return "base-layout";
    }

    @PostMapping("/sync/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String syncExtension(@PathVariable("id") String id) {
        extensionService.sync(Integer.parseInt(id));
        extensionService.reloadLists();
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
    public String deleteExtension(@PathVariable("id") Integer id) {

        Extension extension = extensionService.getById(id);
        if (!extensionService.isUserPublisherOrAdmin(extension)) {
            return "redirect:/extension/" + id;
        }
        extensionService.delete(id);
        userService.reloadMemory();
        return "redirect:/";
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveExtension(@PathVariable("id") Integer id) {
        extensionService.approveExtensionById(id);

        return "redirect:/extension/" + id;
    }

    @PostMapping("/disapprove/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String disapproveExtension(@PathVariable("id") Integer id) {
        extensionService.disapproveExtensionById(id);

        return "redirect:/extension/" + id;
    }


    @PostMapping("/download/{id}")
    @PreAuthorize("isAuthenticated()")
    public String download(@PathVariable("id") Integer id) {

        Extension extension = extensionService.getByIdFromMemory(id);
        extensionService.download(id);

        return "redirect:" + extension.getDlURI();
    }

    @PostMapping("/feature/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String featureExtension(@PathVariable("id") Integer id) {
        if (extensionService.getAdminSelection().size() == 5) {
            return "redirect:/featuredSelection";
        }
        extensionService.setFeatured(id);

        return "redirect:/extension/" + id;
    }

    @PostMapping("/unFeature/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String unFeatureExtension(@PathVariable("id") Integer id) {
        extensionService.removeFeatured(id);

        return "redirect:/extension/" + id;
    }

    @GetMapping("/featuredSelection")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getFeaturedSelection(Model model) {
        if(!userService.getCurrentUser().isAdmin()){
            return "redirect:/";
        }
        model.addAttribute("view", "/admin/featuredSelection");
        model.addAttribute("featured", extensionService.getAdminSelection());
        return "base-layout";
    }

    @PostMapping("/removeFeatured")
    public String removeFeatured(Model model, @RequestParam("list") List<Long> stuff) {
        extensionService.unfeatureList(stuff);

        return "redirect:/admin/panel";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String getExtensionEdit(Model model, @PathVariable long id, @RequestParam(required = false) String error) {
        Extension toEdit = extensionService.getByIdFromMemory(id);
        String tags = "";
        for (Tag t : toEdit.getTags()) {
            tags = tags.concat(t.getName() + ", ");
        }
        if (tags.length() > 1) tags = tags.substring(0, tags.lastIndexOf(", "));
        if (error != null) model.addAttribute("error", error);
        model.addAttribute("toEdit", toEdit);
        model.addAttribute("tags", tags);
        model.addAttribute("view", "/extensions/edit");
        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('USER')")
    public String editExtension(@Valid ExtensionEditModel editModel, BindingResult errors, @PathVariable long id, Model model) {
        if (errors.hasErrors()) {
            return "redirect:/extension/edit/" + id;
        }
        if (!extensionService.edit(editModel, id)) {
            model.addAttribute("error", ErrorMessages.EXTENSION_EDIT_ERROR);
            return "redirect:/extension/edit/" + id;
        }
        userService.reloadMemory();
        return "redirect:/extension/" + id;
    }


}
