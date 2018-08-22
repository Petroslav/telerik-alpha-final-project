package com.alpha.marketplace.controllers;

import com.alpha.marketplace.services.CloudUserServiceImpl;
import com.alpha.marketplace.services.base.CloudUserService;
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

    private CloudUserServiceImpl service;

    @Autowired
    public void setCloudServiceImpl(CloudUserServiceImpl service){
        this.service = service;
    }

    @GetMapping("/upload")
    public String uploadPage(Model model){
        model.addAttribute("view", "testuploadform");
        return "base-layout";
    }

    @PostMapping("/upload/kek")
    public String upload(Model model, @RequestParam("filename") MultipartFile file) throws IOException {
        service.saveUserPic("5", file.getBytes());

        model.addAttribute("view", "index");
        return "base-layout";
    }
}
