package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.binding.UserBindingModel;
import com.alpha.marketplace.services.base.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {

    private ExtensionService extensionService;

    @Autowired
    public HomeController(ExtensionService extensionService){
        this.extensionService = extensionService;
    }

    @GetMapping("/")
    public String index(Model model){
        List<Extension> newestExtensions = extensionService.getLatest();
        List<Extension> selectedByAdmin = extensionService.getAdminSelection();
        List<Extension> mostPopular = extensionService.getMostPopular();

        model.addAttribute("view", "index");
        model.addAttribute("newest", newestExtensions);
        model.addAttribute("adminSelection", selectedByAdmin);
        model.addAttribute("mostPopular", mostPopular);

        return "base-layout";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("view", "register");
        model.addAttribute("user", new UserBindingModel());
        return "base-layout";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("view", "login");
        return "base-layout";
    }

    @GetMapping("/unauthorized")
    public String unauthorized(Model model){
        model.addAttribute("view", "unauthorized");

        return "base-layout";
    }
}