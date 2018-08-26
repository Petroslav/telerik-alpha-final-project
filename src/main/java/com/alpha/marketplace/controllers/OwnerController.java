package com.alpha.marketplace.controllers;

import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    private final UserService service;

    @Autowired
    public OwnerController(UserService service) {
        this.service = service;
    }

    @PostMapping("/assignAdmin")
    @PreAuthorize("hasRole('OWNER')")
    public String assignRole(Model model, @RequestParam("username") String username){
        service.addRoleToUser(username, "ROLE_ADMIN");
        return "redirect:/admin/users";
    }

    @PostMapping("/ban")
    @PreAuthorize("hasRole('OWNER')")
    public String banAccount(Model model, @RequestParam("id") long id){
        service.findById(id).ban();
        return "redirect:/admin/users";
    }

}
