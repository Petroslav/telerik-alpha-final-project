package com.alpha.marketplace.models.dtos;

import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.Tag;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

public class ExtensionDTO {

    private long id;
    private String name;
    private String description;
    private int downloads;
    private String mediaLink;
    private Set<Tag> tags;
    private Date publishDate;
    private GitHubInfo info;

    public ExtensionDTO(long id, String name, String description, int downloads, String mediaLink, Set<Tag> tags, Date publishDate, GitHubInfo info) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.downloads = downloads;
        this.mediaLink = mediaLink;
        this.tags = tags;
        this.publishDate = publishDate;
        this.info = info;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getMediaLink() {
        return mediaLink;
    }

    public void setMediaLink(String mediaLink) {
        this.mediaLink = mediaLink;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public GitHubInfo getInfo() {
        return info;
    }

    public void setInfo(GitHubInfo info) {
        this.info = info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtensionDTO that = (ExtensionDTO) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
