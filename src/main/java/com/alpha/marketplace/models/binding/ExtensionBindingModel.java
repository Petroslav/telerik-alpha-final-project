package com.alpha.marketplace.models.binding;

import org.hibernate.validator.constraints.URL;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

public class ExtensionBindingModel {

    @NotEmpty
    @Size(min = 5, max = 10)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 5000)
    private String description;

    @URL
    @NotEmpty
    private String repositoryUrl;

    @URL
    @NotEmpty
    private String downloadLink;

    @NotEmpty
    private String version;

    @NotEmpty
    private List<String> tags;

    @NotEmpty
    private MultipartFile file;

    @NotEmpty
    private MultipartFile pic;

    public ExtensionBindingModel(
            @NotEmpty @Size(min = 5, max = 10) String name,
            @NotEmpty @Size(min = 20, max = 5000) String description,
            @URL @NotEmpty String repositoryUrl,
            @URL @NotEmpty String downloadLink,
            @NotEmpty String version,
            @NotEmpty List<String> tags,
            @NotEmpty MultipartFile file,
            @NotEmpty MultipartFile pic
    ) {
        this.name = name;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
        this.downloadLink = downloadLink;
        this.version = version;
        this.tags = tags;
        this.file = file;
        this.pic = pic;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getPic() {
        return pic;
    }

    public void setPic(MultipartFile pic) {
        this.pic = pic;
    }
}
