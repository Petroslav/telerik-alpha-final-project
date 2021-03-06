package com.alpha.marketplace.config;

import com.alpha.marketplace.services.base.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserService userService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register", "/css/**", "/extension/**", "/scripts/**", "/tag/**", "/search/**", "/api/**", "/newest/**","/popular/**", "/user/**").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/user/profile/**", "/extension/edit").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/owner/**").hasRole("OWNER")
                .and()
                .formLogin().loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .usernameParameter("username")
                .passwordParameter("pass1")

                .and()
                .rememberMe()
                .rememberMeCookieName("tscodelogin")
                .rememberMeParameter("rememberMe")
                .key("nocluewtfthisissupposedtobe")
                .alwaysRemember(false)
                .and()
                .exceptionHandling().accessDeniedPage("/error/403")
                .and()
                .csrf().disable();

    }

    @Bean
    BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
