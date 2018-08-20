package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UserBindingModel());
        model.addAttribute("view", "register");
        return "base-layout";
    }

    @PostMapping("/register")
    public String regUser(Model model, UserBindingModel user){
        User newUser = service.registerUser(user);
        if(newUser == null){
            model.addAttribute("view", "failReg");
            return "failReg";
        }
        model.addAttribute("user", newUser);
        model.addAttribute("view", "successfulReg");

        return "base-layout";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/logoutPage";
    }

    @GetMapping("/logoutPage")
    public String logoutPage(){
        return "services";
    }
}
