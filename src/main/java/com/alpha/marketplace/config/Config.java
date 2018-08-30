package com.alpha.marketplace.config;

import com.alpha.marketplace.models.*;
import com.alpha.marketplace.repositories.base.PropertiesRepository;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;


import java.io.*;

@Configuration
@EnableScheduling
public class Config {

    private final String PROJECT_ID = "telerikfinalproject";

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
            InputStream is = new ByteArrayInputStream(cr.getBytes());
            Credentials credentials = GoogleCredentials.fromStream(is);
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

    @Autowired
    public void setPropertiesRepository(PropertiesRepository propertiesRepository) {
        this.propertiesRepository = propertiesRepository;
    }
}