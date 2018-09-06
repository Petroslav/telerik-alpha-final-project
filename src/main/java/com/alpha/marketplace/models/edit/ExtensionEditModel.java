package com.alpha.marketplace.models.edit;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class ExtensionEditModel {

    private String name;
    private String description;
    private String version;
    private String repo;
    private MultipartFile file;
    private MultipartFile picture;
    private String tags;
    private Date updateDate;

}
