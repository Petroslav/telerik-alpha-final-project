package com.alpha.marketplace.utils;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URL;

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
}
