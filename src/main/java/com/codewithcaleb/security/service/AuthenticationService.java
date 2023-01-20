package com.codewithcaleb.security.service;


import com.codewithcaleb.security.config.JwtService;
import com.codewithcaleb.security.pojos.AuthenticationRequest;
import com.codewithcaleb.security.pojos.AuthenticationResponse;
import com.codewithcaleb.security.pojos.RegisterRequest;
import com.codewithcaleb.security.user.Role;
import com.codewithcaleb.security.user.User;
import com.codewithcaleb.security.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService  jwtService;

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

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {

    }
}
