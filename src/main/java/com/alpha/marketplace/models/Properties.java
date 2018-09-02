package com.alpha.marketplace.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "properties")
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "properties_id")
    private short id;

    @Column(name = "github_key", nullable = false)
    private String gitHubOAuthKey;

    @Column(name = "project_id")
    private String projectId;

    @Column(name = "credentials", nullable = false, length = 5000)
    private String credentials;

    @Column(name = "delay", nullable = false)
    private long delay;

    @Column(name = "success", nullable = false)
    private Date lastSuccessfulSync;

    @Column(name = "fail", nullable = false)
    private Date lastFailedSync;

    @Column(name = "fail_info", nullable = false)
    private String failInfo;

    public Properties(){}

    public Properties(String gitHubOAuthKey, String projectId, String credentials, long delay, Date lastSuccessfulSync, Date lastFailedSync, String failInfo) {
        this.gitHubOAuthKey = gitHubOAuthKey;
        this.projectId = projectId;
        this.credentials = credentials;
        this.delay = delay;
        this.lastSuccessfulSync = lastSuccessfulSync;
        this.lastFailedSync = lastFailedSync;
        this.failInfo = failInfo;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getGitHubOAuthKey() {
        return gitHubOAuthKey;
    }

    public void setGitHubOAuthKey(String gitHubOAuthKey) {
        this.gitHubOAuthKey = gitHubOAuthKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public Date getLastSuccessfulSync() {
        return lastSuccessfulSync;
    }

    public void setLastSuccessfulSync(Date lastSuccessfulSync) {
        this.lastSuccessfulSync = lastSuccessfulSync;
    }

    public Date getLastFailedSync() {
        return lastFailedSync;
    }

    public void setLastFailedSync(Date lastFailedSync) {
        this.lastFailedSync = lastFailedSync;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }
}
