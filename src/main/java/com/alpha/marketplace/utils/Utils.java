package com.alpha.marketplace.utils;

import com.alpha.marketplace.exceptions.FailedToSyncException;
import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.models.Properties;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GitHub;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Date;


public class Utils {
    public static final String GITHUB_URL_PREFIX = "https://github.com/";

    public static Properties properties;
    private static GitHub GITHUB_CONNECTION;
    private static String gitKey;

    public static boolean userIsAnonymous() {
        return AnonymousAuthenticationToken.class ==
                SecurityContextHolder
                        .getContext()
                        .getAuthentication().getClass();
    }

    public static byte[] getBytesFromUrl(String urlString) {
        byte[] bytes;
        try {
            URL url = new URL(urlString);
            bytes = StreamUtils.copyToByteArray(url.openConnection().getInputStream());
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        return bytes;
    }

    public static String getContentTypeFromUrl(String urlString){
        String contentType;
        try {
            contentType = new URL(urlString).openConnection().getContentType();
        }catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contentType;
    }

    public static void setKey(String key){
        gitKey = key;
    }

    public static void updateGithubInfo(GitHubInfo info) throws FailedToSyncException {
        String url = removePrefix(info.getParent().getRepoURL());
        try{
            info.setIssuesCount(openIssues(url));
            info.setLastCommit(commitDate(url));
            info.setPullCount(pullsCount(url));
        }catch(IOException e){
            throw new FailedToSyncException("Failed to sync GitHub info");
        }

    }

    private static GitHub getGHConnection(){

        try {
            if(GITHUB_CONNECTION == null){
                GITHUB_CONNECTION = GitHub.connectUsingOAuth(gitKey);
                gitKey = "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GITHUB_CONNECTION;
    }

    private static String openIssues(String url) throws IOException {
        GitHub gh = getGHConnection();
        String openIssues;
        openIssues = String.valueOf(gh.getRepository(url).getOpenIssueCount());
        return openIssues;
    }

    private static Date commitDate(String url) throws IOException {
        GitHub gh = getGHConnection();
        Date commitDate;
        commitDate = gh.getRepository(url).listCommits().asList().get(0).getCommitDate();
        return commitDate;
    }

    private static String pullsCount(String url) throws IOException {
        GitHub gh = getGHConnection();
        String pullCount;
        pullCount = String.valueOf(gh.getRepository(url).getPullRequests(GHIssueState.OPEN).size());
        return pullCount;
    }

    private static String removePrefix(String url){
        return url.substring(GITHUB_URL_PREFIX.length());
    }


}