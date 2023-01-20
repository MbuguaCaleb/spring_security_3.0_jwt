package com.codewithcaleb.security.config;

import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final Filter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;



    //At application start up Spring will try to look for a Bean of Type SecurityFilterChain
    //Responsible for configuring all our application security

    //Whitelisted endpoints are endpoints that do not require any token authentication
    //(Example Creating an Account and Logging In)

    //Session Management
    //When we Implemented our Filter,we wanted a once per request filter
    //This means every request should be authenticated
    //This can allow to have a stateless session that does not need to be stored
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("")//all URL(s) Here will be permitted
                .permitAll()
                .anyRequest()//any other request must be authenticated
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) //Spring will create a new session for each request
                .and()
                .authenticationProvider(authenticationProvider) //I need to tell spring the authentication provider i want to use
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); //i want to execute this filter before the username password authentication filter


        return http.build();
    }

}
