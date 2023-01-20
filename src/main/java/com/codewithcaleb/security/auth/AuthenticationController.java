package com.codewithcaleb.security.auth;

import com.codewithcaleb.security.pojos.AuthenticationRequest;
import com.codewithcaleb.security.pojos.AuthenticationResponse;
import com.codewithcaleb.security.pojos.RegisterRequest;
import com.codewithcaleb.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    //register endpoint
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        return  ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    //register endpoint
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest authenticationRequest){
        return  ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
