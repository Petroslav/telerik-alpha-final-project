package com.alpha.marketplace.config;

import com.alpha.marketplace.models.*;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

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

}
