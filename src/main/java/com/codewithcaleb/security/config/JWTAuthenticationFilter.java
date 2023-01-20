package com.codewithcaleb.security.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//The filter will be called once per each request(It is the first thing that intercepts all our requests
//Its like a middleware where all requests will be authenticated
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        private final JwtService jwtService = null;

        //Validating the JWT
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            //we call on next filter and do not  implement any logic
            //Middlewares always call on the Next Method
            filterChain.doFilter(request,response);
            return;
        }
        //If i count beaerer with splace the count is 7
        jwt = authHeader.substring(7 );


        userEmail = jwtService.extractUsername(jwt); // Todo extact user email from JWT token;


    }
}


//What we do in the filter

//1.Checking the JWT Token.

//The jwt auth token is passed as an Authorization Header
//It must start with the Bearer KeyWord