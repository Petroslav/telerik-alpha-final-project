package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.services.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/extension")
public class ExtensionController {

    private ExtensionService extensionService;

    @Autowired
    public ExtensionController(ExtensionService extensionService){
        this.extensionService = extensionService;
    }

    @GetMapping("/all")
    public String getAll(Model model){
        List<Extension> extensions = extensionService.getAllApproved();

        model.addAttribute("view", "extensions/all");
        model.addAttribute("extensions", extensions);

        return "base-layout";
    }
}
