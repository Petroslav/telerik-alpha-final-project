package com.alpha.marketplace.controllers;

import com.alpha.marketplace.exceptions.VersionMismatchException;
import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.edit.UserEditModel;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import com.alpha.marketplace.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService service;
    private final ExtensionService extensionService;

    @Autowired
    public UserController(UserService service, ExtensionService extensionService) {
        this.extensionService = extensionService;
        this.service = service;
    }

    @GetMapping("/{id}")
    public String userDetails(Model model, @PathVariable("id") Integer id) {
        if (getUser().getId() == id) {
            return "redirect:/user/profile";
        }
        User user = service.findByIdFromMemory(id);
        if (user == null) {
            model.addAttribute("view", "error/404");
            return "base-layout";
        }
        User currentUser = service.getCurrentUser();

        if (id == currentUser.getId()) {
            return "redirect:/user/profile";
        }
        Set<Extension> extensions = new HashSet<>(user.getExtensions());
        model.addAttribute("user", user);
        model.addAttribute("extensions", extensions);
        model.addAttribute("view", "user/details");

        return "base-layout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model) {
        User u = getUser();
        if (u == null) {
            service.reloadMemory();
            u = getUser();
        }
        List<Extension> extensions = u.getExtensions();

        List<Extension> pending = extensions.stream()
                .filter(e -> !e.isApproved())
                .collect(Collectors.toList());
        extensions = extensions.stream()
                .filter(Extension::isApproved)
                .collect(Collectors.toList());

        model.addAttribute("user", u);
        model.addAttribute("approved", extensions);
        model.addAttribute("pending", pending);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }

    @PostMapping("/update")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@ModelAttribute UserEditModel userEdit) throws VersionMismatchException {
        User u = getUser();
        service.editUser(u, userEdit);
        return "redirect:/profile";
    }


    @PostMapping("/update/pic/file")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@RequestParam("picFile") MultipartFile file) {
        User u = getUser();
        if (!file.isEmpty()) {
            service.editUserPic(u, file);
        }
        //TODO HANDLE ERROR IF FILE TOO BIG OR BAD FILE FORMAT

        return "redirect:/profile";
    }

    @PostMapping("/update/pic/url")
    @PreAuthorize("isAuthenticated()")
    public String updateProfile(@RequestParam("picUrl") String urlString) {
        User u = getUser();
        service.editUserPic(u, urlString);
        //TODO HANDLE ERROR IF BAD URL OR TOO BIG
        return "redirect:/profile";
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfilePage(Model model) {
        if (Utils.userIsAnonymous()) {
            return "redirect:/";
        }
        model.addAttribute("view", "user/edit");
        return "base-layout";
    }

    @PostMapping("/profile/edit")
    @PreAuthorize("isAuthenticated()")
    public String editProfile(UserEditModel editModel) throws VersionMismatchException {

        if (!service.editUser(service.getCurrentUser(), editModel)) {
            return "redirect:/user/profile/edit";
        }
        extensionService.reloadLists();
        service.reloadMemory();
        return "redirect:/user/profile";
    }

    private User getUser() {
        User user = (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return service.findByIdFromMemory(user.getId());
    }
}
