package com.webdev.jobify.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf().disable().authorizeRequests().anyRequest().permitAll();
//        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http
                .cors().and()
                .authorizeRequests()
//                .antMatchers("/admin/**").authenticated()
                .and().formLogin().loginPage("/login").permitAll()
                .and().logout();
        http.csrf().disable();
        http.headers().frameOptions().disable();
    }
}