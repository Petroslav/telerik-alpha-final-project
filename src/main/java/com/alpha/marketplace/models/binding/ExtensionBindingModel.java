package com.alpha.marketplace.models.binding;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class ExtensionBindingModel {

    @NotEmpty
    @Size(min = 5, max = 10)
    private String name;

    @NotEmpty
    @Size(min = 20, max = 5000)
    private String description;

    @URL
    @NotEmpty
    private String repositoryUrl;

    @URL
    @NotEmpty
    private String downloadLink;

    @NotEmpty
    private List<String> tags;

    public ExtensionBindingModel(String name, String description, String repositoryUrl,  String downloadLink, List<String> tags) {
        this.name = name;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
        this.downloadLink = downloadLink;
        this.tags = tags;
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

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
