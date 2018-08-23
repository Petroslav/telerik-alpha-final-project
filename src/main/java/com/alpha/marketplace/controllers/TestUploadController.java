package com.alpha.marketplace.controllers;

import com.alpha.marketplace.services.CloudUserServiceImpl;
import com.alpha.marketplace.services.base.CloudUserService;
import com.google.api.client.http.HttpRequest;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Controller
public class TestUploadController {

    private CloudUserService service;

    @Autowired
    public void setCloudServiceImpl(CloudUserService service){
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
