package com.alpha.marketplace.models;

import com.google.api.client.json.Json;
import com.google.gson.JsonObject;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "properties")
public class Properties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "properties_id")
    private short id;

    @Column(name = "delay", nullable = false)
    private String delay;

    @Column(name = "github_key", nullable = false)
    private String gitHubOAuthKey;

    @Column(name = "success", nullable = false)
    private Date lastSuccessfulSync;

    @Column(name = "fail", nullable = false)
    private Date lastFailedSync;

    @Column(name = "fail_info", nullable = false)
    private String failInfo;

    @Column(name = "credentials", nullable = false, length = 5000)
    private String credentials;

    public Properties(){}

    public Properties(String delay, String gitHubOAuthKey, Date lastSuccessfulSync, Date lastFailedSync, String failInfo, String credentials) {
        this.delay = delay;
        this.gitHubOAuthKey = gitHubOAuthKey;
        this.lastSuccessfulSync = lastSuccessfulSync;
        this.lastFailedSync = lastFailedSync;
        this.failInfo = failInfo;
        this.credentials = credentials;
    }

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public String getGitHubOAuthKey() {
        return gitHubOAuthKey;
    }

    public void setGitHubOAuthKey(String gitHubOAuthKey) {
        this.gitHubOAuthKey = gitHubOAuthKey;
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

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}
