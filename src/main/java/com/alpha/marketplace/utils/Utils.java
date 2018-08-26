package com.alpha.marketplace.utils;

import com.alpha.marketplace.models.GitHubInfo;
import com.alpha.marketplace.repositories.base.ExtensionRepository;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Utils {

    public static boolean isUserNotAnonymous() {
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

    public static void setGithubInfo(GitHubInfo info){
        String url = removePrefix(info.getParent().getRepoURL());

        info.setIssuesCount(openIssues(url));
        info.setLastCommit(commitDate(url));
        info.setPullCount(pullsCount(url));

    }

    private static GitHub getGHConnection(){
        GitHub gh = null;
        try {
            gh = GitHub.connectUsingOAuth("5c1a77eec3047ae6b562a55a7c0e4d4735cb38ef");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gh;
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

    private static String commitDate(String url){
        GitHub gh = getGHConnection();
        String commitDate = null;
        String pattern = "yyyy/MM/dd, hh:mm:ss a";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        try {
            commitDate = simpleDateFormat.format(new Date(gh.getRepository(url).listCommits().asList().get(0).getCommitDate().getTime()));
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
        String prefix = "https://github.com/";
        return url.substring(prefix.length());
    }
}
