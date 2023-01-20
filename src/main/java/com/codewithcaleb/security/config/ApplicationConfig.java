package com.codewithcaleb.security.config;

import com.codewithcaleb.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    //it will override the default userDetails Service Bean
    @Bean
    public UserDetailsService userDetailsService(){
        return new  UserDetailsService(){
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                //we want to get the User from the Database
                //Find byEmail returns an Optional that is why i am having orElse
                return userRepository.findByEmail(username)
                        .orElseThrow(()->new UsernameNotFoundException("User not found")) ;
            }
        };
    }

    //It is Data Access Object responsible to fetch the user details and also encode password
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        //we need to tell it which userDetails service to use in order to fetch information About our users
        //We may have mutltiple implementations of userDetails
        //ie.one getting info from DB Like Ours, Another may be an in memory DB /ldap etc
        authProvider.setUserDetailsService(userDetailsService());

        //if i have a userName and password and i need decode a user
        //i need to have the correct algorithm set so that i inform spring security how to decode
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }


    //Authentication Manager
    //Is used to manage the authentication
    //We can authenticate a user based on username and password
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
