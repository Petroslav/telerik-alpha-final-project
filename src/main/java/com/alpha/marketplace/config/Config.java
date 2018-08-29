package com.alpha.marketplace.config;

import com.alpha.marketplace.models.*;
import com.google.auth.Credentials;
import com.google.auth.oauth2.AccessToken;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@EnableScheduling
public class Config {

    private final String PATH_TO_CONFIG = "C:\\Users\\luffy\\OneDrive\\Desktop\\marketplace\\src\\main\\resources\\telerikfinalproject-30ecbd8e72f6.json";
    private final String PATH_TO_CONFIG_2 = "C:\\Users\\Fast1r1s\\Desktop\\telerik-alpha-final-project\\src\\main\\resources\\telerikfinalproject-30ecbd8e72f6.json";
    private final String PROJECT_ID = "telerikfinalproject";

    @Bean
    SessionFactory getSessionFactory() {
        return new org.hibernate.cfg.Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Extension.class)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(Role.class)
                .addAnnotatedClass(GitHubInfo.class)
                .buildSessionFactory();
    }

    @Bean
    ModelMapper mapper() {
        return new ModelMapper();
    }

    @Bean
    Storage getStorage() {
        String actualPath = PATH_TO_CONFIG;
        try {
            File file = new File(actualPath);
            if (!file.exists()) {
                actualPath = PATH_TO_CONFIG_2;
            }
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(actualPath));
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