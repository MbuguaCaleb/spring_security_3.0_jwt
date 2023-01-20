package com.codewithcaleb.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    //The security signing key  required for JWT is 256 bit
    private static  final String SECRET_KEY = "26452948404D635166546A576E5A7134743777217A25432A462D4A614E645267";

   // a Json Web Token Contains three parts, Header,Claims and the Signature
   //header contains the signing key
    //To generate/decode a token i need a signing key
    public String extractUsername(String token){
        //The Subject should be my username or email of myUser
        return extractClaim(token,Claims::getSubject);
    }


    //A method that can extract a Single Claim that we Pass
    //This is a generic method that i can use to extract anyClaim from myToken
    public  <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }


    //Remember claims is what is contained in the JSON Body
    //It is what carries meaningful information

    //A signing key is a secret that is used to digitally sign the JWT
    //It ensures that the user is who they clam to be and that the token
    //was not changed along the way

    //The Signing Key is used in conjunction with the Signing Algorithm specified
    // in the JWT header to create the signature
    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //Signing Key Method
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    //Other Important JWT Methods
    //The Map will contain the info i want to store within the token
    //claims Objects which we want to add
    public String generateToken(Map<String, Object> extraClaims,
                                UserDetails userDetails){

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) //For spring whe  subject is the username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000* 60* 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    //Generating a Token without passing Claims
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }

    //Method that can validate the token
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(extractUsername(token));
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //everything extracted from the Claims is coming from the Claims Class
    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }
}
