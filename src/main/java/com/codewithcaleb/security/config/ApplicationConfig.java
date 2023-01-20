package com.codewithcaleb.security.config;

import com.codewithcaleb.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
}
