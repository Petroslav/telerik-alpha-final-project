package com.alpha.marketplace.models.binding;

import com.alpha.marketplace.models.Tag;
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

    @NotEmpty
    private String version;

    @NotEmpty
    private List<String> tags;

    @NotEmpty
    private MultipartFile file;

    @NotEmpty
    private MultipartFile pic;

    @NotEmpty
    private String tagString;

    public ExtensionBindingModel(
            @NotEmpty @Size(min = 5, max = 10) String name,
            @NotEmpty @Size(min = 20, max = 5000) String description,
            @URL @NotEmpty String repositoryUrl,
            @NotEmpty String version,
            @NotEmpty List<String> tags,
            @NotEmpty MultipartFile file,
            @NotEmpty MultipartFile pic,
            @NotEmpty String tagString
    ) {
        this.name = name;
        this.description = description;
        this.repositoryUrl = repositoryUrl;
        this.version = version;
        this.tags = tags;
        this.file = file;
        this.pic = pic;
        this.tagString = tagString;
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

    public String getTagString() {
        return tagString;
    }

    public void setTagString(String tagString) {
        this.tagString = tagString;
    }
}
