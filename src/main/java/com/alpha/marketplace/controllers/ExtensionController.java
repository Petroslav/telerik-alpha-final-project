package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.binding.ExtensionBindingModel;
import com.alpha.marketplace.services.base.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/extension")
public class ExtensionController {

    private ExtensionService extensionService;

    @Autowired
    public ExtensionController(ExtensionService extensionService) {
        this.extensionService = extensionService;
    }

    @GetMapping("/all")
    public String getAll(Model model) {
        List<Extension> extensions = extensionService.getAllApproved();

        model.addAttribute("view", "extensions/all");
        model.addAttribute("extensions", extensions);

        return "base-layout";
    }

    @GetMapping("/create")
    public String createPage(Model model) {
        model.addAttribute("view", "extensions/create");
        return "base-layout";
    }
    @PostMapping("/create")
    public String create(ExtensionBindingModel model){
        extensionService.createExtension(model);

        return "redirect:/";
    }
}
