package com.codewithcaleb.security.config;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//The filter will be called once per each request(It is the first thing that intercepts all our requests
//Its like a middleware where all requests will be authenticated
@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private  final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

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


        //Extract user email from JWT token(via utils);
        userEmail = jwtService.extractUsername(jwt);

        //there is no need to re-authenticate if the user already is authenticated
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //Yes i can extract the email from JWT but its not for an existing user
            //i also must check if the user is in the database
            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

            //validate the token
            if(jwtService.isTokenValid(jwt,userDetails)){
                //update the security-context and forward the request to dispatcher servlet
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, //since we don't have credentials, that's why we are passing null
                        userDetails.getAuthorities()
                );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                //update security context holder
                SecurityContextHolder.getContext().setAuthentication(authToken);

                filterChain.doFilter(request,response);
            }
        }


    }
}


//What we do in the filter

//1.Checking the JWT Token.

//The jwt auth token is passed as an Authorization Header
//It must start with the Bearer KeyWord