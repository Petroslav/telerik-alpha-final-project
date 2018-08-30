package com.alpha.marketplace.config;

import com.alpha.marketplace.models.*;
import com.alpha.marketplace.repositories.base.PropertiesRepository;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.FileUtils;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

@Configuration
@EnableScheduling
public class Config {

    private final String PROJECT_ID = "telerikfinalproject";

    @Autowired
    private PropertiesRepository propertiesRepository;

    @Bean
    SessionFactory getSessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Extension.class)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(GitHubInfo.class)
                .addAnnotatedClass(Properties.class)
                .buildSessionFactory();
    }

    @Bean
    ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    Storage getStorage() {
        try {
            String cr = propertiesRepository.get().getCredentials();
            URL crURL = new URL(cr);
            System.out.println(crURL.toString());
            System.out.println(cr);
            Credentials credentials = GoogleCredentials.fromStream(crURL.openStream());
            return StorageOptions.newBuilder().setCredentials(credentials).setProjectId(PROJECT_ID).build().getService();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }


}