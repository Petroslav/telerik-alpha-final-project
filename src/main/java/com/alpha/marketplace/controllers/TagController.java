package com.alpha.marketplace.controllers;

import com.alpha.marketplace.models.Extension;
import com.alpha.marketplace.models.Tag;
import com.alpha.marketplace.repositories.base.TagRepository;
import com.alpha.marketplace.services.base.ExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tag")
public class TagController {

    private TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    @GetMapping("/{name}")
    public String tagPage(Model model, @PathVariable("name") String name){
        Tag tag = tagRepository.findByName(name);
        List<Extension> extensions = tag.getTaggedExtensions()
                .stream()
                .filter(Extension::isApproved)
                .collect(Collectors.toList());

        model.addAttribute("tag", tag);
        model.addAttribute("extensions", extensions);
        model.addAttribute("view", "tag/details");

        return "base-layout";
    }

}
