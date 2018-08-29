package com.alpha.marketplace.models;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name="git_info")
public class GitHubInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "gitHubInfo")
    private Extension parent;

    @Column(name = "pulls")
    private String pullCount;

    @Column(name = "issues")
    private String issuesCount;

    @Column(name = "latest_commit")
    private Date lastCommit;

    public GitHubInfo(){

    }

    public GitHubInfo(Extension parent, String pullCount, String issuesCount, Date lastCommit) {
        this.parent = parent;
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

    public Extension getParent() {
        return parent;
    }

    public void setParent(Extension parent) {
        this.parent = parent;
    }

    public String getPullCount() {
        return pullCount;
    }

    public void setPullCount(String pullCount) {
        this.pullCount = pullCount;
    }

    public String getIssuesCount() {
        return issuesCount;
    }

    public void setIssuesCount(String issuesCount) {
        this.issuesCount = issuesCount;
    }

    public Date getLastCommit() {
        return lastCommit;
    }

    public void setLastCommit(Date lastCommit) {
        this.lastCommit = lastCommit;
    }

    public String getLastCommitToString(){
        String pattern = "yyyy/MM/dd, hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(lastCommit);
    }
}
