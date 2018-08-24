package com.alpha.marketplace.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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


    public static String openIssues(String owner, String repo){
        String openIssues = null;
        Scanner sc = null;
        try{
            //make a separate HTTP request to a public api
            URL url = new URL("https://api.github.com/repos/" + owner +"/" + repo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            //work with the response
            sc = new Scanner(connection.getInputStream());
            StringBuilder result = new StringBuilder();
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            System.out.println("Result = " + result);
            //return the jsp showing this response

            String json = result.toString();
            JsonObject jsonObj = new JsonParser().parse(json).getAsJsonObject();
            openIssues = jsonObj.get("open_issues_count").getAsString();
        }catch(Exception e){
            System.out.println("Sadface");
            System.out.println(e.getMessage());
        }finally{
            if(sc != null) sc.close();
        }
        return openIssues;
    }

    public static String commitDate(String repo, String owner){
        String commitDate = null;
        Scanner sc = null;
        try{
            //make a separate HTTP request to a public api
            URL url = new URL("https://api.github.com/repos/" + owner +"/" + repo + "/commits");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            //work with the response
            sc = new Scanner(connection.getInputStream());
            StringBuilder result = new StringBuilder();
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            String json = result.toString();
            JsonArray jsonObj = new JsonParser().parse(json).getAsJsonArray();
            commitDate = jsonObj.get(0).getAsJsonObject().get("commit").getAsJsonObject().get("author").getAsJsonObject().get("date").getAsString();
            commitDate = commitDate.substring(0, 10) + ", " + commitDate.substring(11, commitDate.length()-1);

        }catch(Exception e){
            System.out.println("Sadface");
            System.out.println(e.getMessage());
        }finally{
            if(sc != null) sc.close();
        }
        return commitDate;
    }

    public static void pullsCount(String owner, String repo){
        String pullCount = null;
        Scanner sc = null;
        try{
            //make a separate HTTP request to a public api
            URL url = new URL("https://api.github.com/repos/" + owner +"/" + repo + "/pulls?q=is%3Aopen+is%3Apr");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            //work with the response
            System.out.println("STATUS CODE = " + connection.getResponseCode());
            System.out.println("response length = " + connection.getContentLength());
            sc = new Scanner(connection.getInputStream());
            StringBuilder result = new StringBuilder();
            while(sc.hasNextLine()){
                result.append(sc.nextLine());
            }
            String json = result.toString();
            JsonArray jsonObj = new JsonParser().parse(json).getAsJsonArray();
            System.out.println(jsonObj.size());
        }catch(Exception e){
            System.out.println("Sadface");
            System.out.println(e.getMessage());
        }finally{
            if(sc != null) sc.close();
        }
    }
}
