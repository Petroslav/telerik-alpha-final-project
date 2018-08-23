package com.alpha.marketplace.controllers;

import com.alpha.marketplace.repositories.base.CloudUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class TestUploadController {

    private CloudUserRepository service;

    @Autowired
    public void setCloudServiceImpl(CloudUserRepository service){
        this.service = service;
    }

    @GetMapping("/upload")
    public String uploadPage(Model model){
        model.addAttribute("view", "testuploadform");
        return "base-layout";
    }

    @PostMapping("/upload/kek")
    public String upload(Model model, @RequestParam("filename") MultipartFile file) throws IOException {
        service.saveUserPic("5", file.getBytes(), file.getContentType());
        model.addAttribute("view", "index");
        return "base-layout";
    }
}
