package com.alpha.marketplace.models;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="git_info")
public class GitHubInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "gitHubInfo")
    private Extension extension;

    @Column(name = "pulls")
    private int pullCount;

    @Column(name = "issues")
    private int issuesCount;

    @Column(name = "latest_commit")
    private Date lastCommit;

    public GitHubInfo(){

    }

    public GitHubInfo(Extension extension, int pullCount, int issuesCount, Date lastCommit) {
        this.extension = extension;
        this.pullCount = pullCount;
        this.issuesCount = issuesCount;
        this.lastCommit = lastCommit;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Extension getExtension() {
        return extension;
    }

    public void setExtension(Extension extension) {
        this.extension = extension;
    }

    public int getPullCount() {
        return pullCount;
    }

    public void setPullCount(int pullCount) {
        this.pullCount = pullCount;
    }

    public int getIssuesCount() {
        return issuesCount;
    }

    public void setIssuesCount(int issuesCount) {
        this.issuesCount = issuesCount;
    }

    public Date getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(Date lastCommit) {
        this.lastCommit = lastCommit;
    }
}
