package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Collectors;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    private final UserService service;

    @Autowired
    public OwnerController(UserService service) {
        this.service = service;
    }

    @PostMapping("/assignAdmin/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public String assignRole(Model model, @PathVariable("id") long id){
        service.addRoleToUser(id, "ROLE_ADMIN");
        return "redirect:/admin/users";
    }

    @PostMapping("/removeAdmin/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public String removeAdmin(Model model, @PathVariable("id") long id){
        User u = service.findById(id);
        if(!u.isAdmin()){
            return "redirect:/";
        }
        service.removeRoleFromUser(id, "ROLE_ADMIN");
        model.addAttribute("view", "panel");
        return "base-layout";
    }

}
