package com.alpha.marketplace;

import com.alpha.marketplace.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class testwtf {
    public static void main(String[] args) {
        System.out.println(Utils.getOwnerFromGitURL("https://github.com/adriyanmihaylov/NGPuppies"));
        System.out.println(Utils.getRepoFromGitURL("https://github.com/adriyanmihaylov/NGPuppies"));
    }
}