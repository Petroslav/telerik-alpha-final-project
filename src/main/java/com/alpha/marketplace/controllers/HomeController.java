package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.repositories.base.ExtensionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private ExtensionRepository extensionRepository;

    @Autowired
    public HomeController(ExtensionRepository extensionRepository){
        this.extensionRepository = extensionRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        List<Extension> newestExtensions = extensionRepository.getNewest();
        List<Extension> selectedByAdmin = extensionRepository.getSelectedByAdmin();
        List<Extension> mostPopular = extensionRepository.getMostPopular();

        model.addAttribute("view", "index");
        model.addAttribute("newest", newestExtensions);
        model.addAttribute("adminSelection", selectedByAdmin);
        model.addAttribute("mostPopular", mostPopular);

        return "base-layout";
    }
}
