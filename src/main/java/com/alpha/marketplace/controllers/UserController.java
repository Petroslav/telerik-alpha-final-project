package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.services.UserService;
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

    @GetMapping("/registerPage")
    public String registerPage(Model model){

        return "register";
    }

    @PostMapping("/register")
    public String register(UserBindingModel model){
        if(service.registerUser(model) == null){
            return "failReg";
        }

        return "successfulReg";
    }

    @PostMapping("/login")
    public String login(){
        return "redirect:/index";
    }

    @PostMapping("/logout")
    public String logout(){
        return "redirect:/logoutPage";
    }

    @GetMapping("/logoutPage")
    public String logoutPage(){
        return "sad";
    }
}