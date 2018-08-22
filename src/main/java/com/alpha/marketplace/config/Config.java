package com.alpha.marketplace.config;

import com.alpha.marketplace.models.*;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class Config {

    private final String pathToConfig = "C:\\Users\\luffy\\OneDrive\\Desktop\\marketplace\\src\\main\\resources\\telerikfinalproject-30ecbd8e72f6.json";
    private final String secondPathToConfig = "C:\\Users\\Fast1r1s\\Desktop\\telerik-alpha-final-project\\src\\main\\resources\\telerikfinalproject-30ecbd8e72f6.json";
    private final String projectId = "telerikfinalproject";

    @Bean
    SessionFactory getSessionFactory(){
        return new org.hibernate.cfg.Configuration()
                .configure()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Extension.class)
                .addAnnotatedClass(Tag.class)
                .addAnnotatedClass(Role.class)
                .buildSessionFactory();
    }

    @Bean
    ModelMapper mapper(){
        return new ModelMapper();
    }

    @Bean
    Storage getStorage() {
        String actualPath = pathToConfig;
        try{
            File file = new File(actualPath);
            if(!file.exists()){

                actualPath = secondPathToConfig;
            }
            Credentials credentials = GoogleCredentials.fromStream(new FileInputStream(actualPath));
            return StorageOptions.newBuilder().setCredentials(credentials).setProjectId(projectId).build().getService();
        }catch(IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

}
