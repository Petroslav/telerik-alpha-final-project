package com.alpha.marketplace.config;

import com.alpha.marketplace.models.*;
import com.alpha.marketplace.repositories.PropertiesRepositoryImpl;
import com.alpha.marketplace.repositories.base.PropertiesRepository;
import com.alpha.marketplace.utils.Utils;
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
            new PropertiesRepositoryImpl(getSessionFactory());
            String cr = Utils.properties.getCredentials();
            InputStream is = new ByteArrayInputStream(cr.getBytes());
            Credentials credentials = GoogleCredentials.fromStream(is);
            String projectId = Utils.properties.getProjectId();
            return StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
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