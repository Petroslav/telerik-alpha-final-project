package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.User;
import com.alpha.marketplace.services.base.ExtensionService;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final ExtensionService extensionService;

    @Autowired
    public AdminController(UserService userService, ExtensionService extensionService) {
        this.userService = userService;
        this.extensionService = extensionService;
    }

    @GetMapping("/unapproved")
    @PreAuthorize("hasRole('ADMIN')")
    public String unapproved(Model model){
        List<Extension> extensions = extensionService.getUnapproved();

        model.addAttribute("extensions", extensions);
        model.addAttribute("view", "/admin/unapproved");

        return "base-layout";
    }

        @GetMapping("/panel")
        @PreAuthorize("hasRole('ADMIN')")
        public String panel(Model model){

        model.addAttribute("view", "/admin/panel");

        return "base-layout";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public String users(Model model){
        List<User> users = userService.getAll();

        model.addAttribute("users", users);
        model.addAttribute("view", "/admin/users");

        return "base-layout";
    }

    @PostMapping("/ban/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String banUser(Model model, @PathVariable("id") long id){
        User gettingBanned = userService.findById(id);
        if(!gettingBanned.isAdmin() || userService.currentUser().isOwner()){
            gettingBanned.ban();
            userService.updateUser(gettingBanned);
        }else{
            System.out.println("Can't ban the guy, he's an admin");
            return "redirect:/admin/users";
        }
        return "redirect:/user/"+id;
    }

    @PostMapping("/unban/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String unbanUser(Model model, @PathVariable("id") long id){
        User gettingUnbanned = userService.findById(id);
        gettingUnbanned.unban();
        userService.updateUser(gettingUnbanned);
        model.addAttribute("view", "index");

        return "redirect:/user/"+id;
    }
}
