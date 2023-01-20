package com.codewithcaleb.security.service;


import com.codewithcaleb.security.config.JwtService;
import com.codewithcaleb.security.pojos.AuthenticationRequest;
import com.codewithcaleb.security.pojos.AuthenticationResponse;
import com.codewithcaleb.security.pojos.RegisterRequest;
import com.codewithcaleb.security.user.Role;
import com.codewithcaleb.security.user.User;
import com.codewithcaleb.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService  jwtService;
    private final AuthenticationManager authenticationManager;

    //create a user
    //save the User to the DB
    //Return generated token
    public AuthenticationResponse register(RegisterRequest registerRequest) {

        //building a user obj that i can save to DB
        var user = User.builder()
                .firstname(registerRequest.getFirstname())
                .lastname(registerRequest.getLastname())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .build();

        //saving the user
        userRepository.save(user);

        //At the moment i do not need to set extra claims
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    //does everything for me and incase username and password is incorrect an exception is thrown
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        //authentication manager bean has a method called authenticate
        //It allows us to authenticate a user based on username and password

        //auth manager does everything for me and incase username and password are
        //incorrect it throws an exception
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(),
                authenticationRequest.getPassword()
        ));

        //cannot reach here if auth details were incorrect
        var user = userRepository.findByEmail(authenticationRequest.getEmail()).orElseThrow();

        //At the moment i do not need to set extra claims
        var jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder().token(jwtToken).build();

    }
}
