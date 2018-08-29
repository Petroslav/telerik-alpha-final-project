package com.alpha.marketplace.utils;

import com.alpha.marketplace.models.GitHubInfo;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GitHub;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static final String GITHUB_URL_PREFIX = "https://github.com/";
    private static GitHub GITHUB_CONNECTION;

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

    public static void updateGithubInfo(GitHubInfo info){
        String url = removePrefix(info.getParent().getRepoURL());

        info.setIssuesCount(openIssues(url));
        info.setLastCommit(commitDate(url));
        info.setPullCount(pullsCount(url));

    }

    private static GitHub getGHConnection(){
        try {
            if(GITHUB_CONNECTION == null){
                GITHUB_CONNECTION = GitHub.connectUsingOAuth("baaf4d97deaba2886b58fdeb52236dfdbe373eb5 ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return GITHUB_CONNECTION;
    }


    private static String openIssues(String url){
        GitHub gh = getGHConnection();
        String openIssues = null;
        try {
            openIssues = String.valueOf(gh.getRepository(url).getOpenIssueCount());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        return openIssues;
    }

    private static Date commitDate(String url){
        GitHub gh = getGHConnection();
        Date commitDate = null;
        try {
            commitDate = gh.getRepository(url).listCommits().asList().get(0).getCommitDate();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return commitDate;
    }

    private static String pullsCount(String url){
        GitHub gh = getGHConnection();
        String pullCount = null;
        try {
            pullCount = String.valueOf(gh.getRepository(url).getPullRequests(GHIssueState.OPEN).size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pullCount;
    }

    private static String removePrefix(String url){
        return url.substring(GITHUB_URL_PREFIX.length());
    }
}