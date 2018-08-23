package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/public/register")
    public String regUser(Model model, @Valid @ModelAttribute UserBindingModel user, BindingResult result){
        System.out.println("Controller reached");
        User newUser = service.registerUser(user);
        if(newUser == null){
            model.addAttribute("view", "failReg");
            return "failReg";
        }
        model.addAttribute("user", newUser);
        model.addAttribute("view", "successfulReg");

        return "base-layout";
    }
    @GetMapping("/{id}")
    public String userDetails(Model model, @PathVariable("id") Integer id){
        User user = service.findById(id);
        if(user == null){
            model.addAttribute("view", "error/404");
            return "base-layout";
        }
        model.addAttribute("user", user);
        model.addAttribute("view", "user/details");

        return "base-layout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User u = (User)service.loadUserByUsername(user.getUsername());


        model.addAttribute("user", u);
        model.addAttribute("view", "user/profile");

        return "base-layout";
    }
}
