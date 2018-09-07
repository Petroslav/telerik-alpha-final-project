package com.alpha.marketplace.models.edit;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

public class ExtensionEditModel {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    @NotEmpty
    private String version;

    @NotEmpty
    private String repo;

    private String tags;

    private MultipartFile file;
    private MultipartFile picture;

    public ExtensionEditModel(@NotEmpty String name, @NotEmpty String description, @NotEmpty String version, @NotEmpty String repo, String tags, MultipartFile file, MultipartFile picture) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.repo = repo;
        this.tags = tags;
        this.file = file;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
    }
}
