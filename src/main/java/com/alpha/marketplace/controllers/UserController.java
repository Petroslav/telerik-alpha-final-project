package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.User;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/user/{id}")
    public String userDetails(Model model, @PathVariable("id") Integer id){


        return "base-layout";
    }
}
