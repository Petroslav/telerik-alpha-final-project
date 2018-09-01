package com.alpha.marketplace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.cloud.storage.BlobId;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "extensions")
public class Extension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "extension_id")
    private long id;

    @Column(name = "extension_name")
    private String name;

    @Column(name = "description", nullable = false, length = 5000)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User publisher;

    @Column(name = "downloads", nullable = false)
    private int downloads;

    @Column(name = "version", nullable = false)
    private String version;

    @JsonIgnore
    @Column(name = "blob_id", columnDefinition = "LONGBLOB")
    private BlobId blobId;

    @JsonIgnore
    @Column(name = "pic_blob_id", columnDefinition = "LONGBLOB")
    private BlobId picBlobId;

    @ManyToMany(
            fetch = FetchType.EAGER,
            cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
    })
    @JoinTable(name = "tagged_extensions",
            joinColumns = @JoinColumn(name = "extension_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    @Column(name = "publish_date", nullable = false)
    private Date addedOn;

    @Column(name = "update_date")
    private Date latestUpdate;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Column(name = "picture", nullable = false)
    private String picURI;

    @Column(name = "dl_uri", nullable = false)
    private String dlURI;

    @Column(name = "repo_url", nullable = false)
    private String repoURL;

    @OneToOne(fetch = FetchType.EAGER,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    @JoinColumn(name = "git_info_id")
    private GitHubInfo gitHubInfo;

    @Column(name = "selected")
    private boolean selected;

    @Column(name = "selection_date")
    private Date selectionDate;

    //TODO fix peasant way of getting githubinfo

    public Extension(){
        setDownloads(0);
        setAddedOn(new Date());
        setApproved(false);
        setTags(new HashSet<>());
        setSelected(false);
    }

    public Extension(
            String name,
            String description,
            User publisher,
            int downloads,
            String version,
            BlobId blobId,
            BlobId picBlobId,
            Set<Tag> tags,
            Date addedOn,
            Date latestUpdate,
            boolean isApproved,
            String picURI,
            String dlURI,
            String repoURL,
            GitHubInfo gitHubInfo,
            boolean selected,
            Date selectionDate
        ) {
        this.name = name;
        this.description = description;
        this.publisher = publisher;
        this.downloads = downloads;
        this.version = version;
        this.blobId = blobId;
        this.picBlobId = picBlobId;
        this.tags = tags;
        this.addedOn = addedOn;
        this.latestUpdate = latestUpdate;
        this.isApproved = isApproved;
        this.picURI = picURI;
        this.dlURI = dlURI;
        this.repoURL = repoURL;
        this.gitHubInfo = gitHubInfo;
        this.selected = selected;
        this.selectionDate = selectionDate;
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


    public User getPublisher() {
        return publisher;
    }
    @JsonProperty("publisher")
    public String getPublisherUserName(){
        return publisher.getUsername();
    }

    @JsonIdentityReference
    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public int getDownloads() {
        return downloads;
    }

    public void setDownloads(int downloads) {
        this.downloads = downloads;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public BlobId getBlobId() {
        return blobId;
    }

    public void setBlobId(BlobId blobId) {
        this.blobId = blobId;
    }

    public BlobId getPicBlobId() {
        return picBlobId;
    }

    public void setPicBlobId(BlobId picBlobId) {
        this.picBlobId = picBlobId;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(Date addedOn) {
        this.addedOn = addedOn;
    }

    public Date getLatestUpdate() {
        return latestUpdate;
    }

    public void setLatestUpdate(Date latestUpdate) {
        this.latestUpdate = latestUpdate;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getPicURI() {
        return picURI;
    }

    public void setPicURI(String picURI) {
        this.picURI = picURI;
    }

    public String getDlURI() {
        return dlURI;
    }

    public void setDlURI(String dlURI) {
        this.dlURI = dlURI;
    }

    public String getRepoURL() {
        return repoURL;
    }

    public void setRepoURL(String repoURL) {
        this.repoURL = repoURL;
    }

    public GitHubInfo getGitHubInfo() {
        return gitHubInfo;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Date getSelectionDate() {
        return selectionDate;
    }

    public void setSelectionDate(Date selectionDate) {
        this.selectionDate = selectionDate;
    }

    public void setGitHubInfo(GitHubInfo gitHubInfo) {
        this.gitHubInfo = gitHubInfo;
    }

    public void approve(){
        isApproved = true;
    }

    public void forbid() {isApproved = false;}

    public boolean isUnApproved(){
        return !isApproved;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Extension extension = (Extension) o;
        return getId() == extension.getId();
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId());
    }
}
