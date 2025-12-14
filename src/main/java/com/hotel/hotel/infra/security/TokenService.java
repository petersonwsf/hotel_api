package com.hotel.hotel.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hotel.hotel.domain.user.User;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    
    public String createToken(User user) {
        
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                .withIssuer("hotel_api")
                .withSubject(user.getLogin())
                .withClaim("id", user.getId())
                .withClaim("name", user.getName())
                .withClaim("role", user.getRole().name())
                .withExpiresAt(createExpireToken())
                .sign(algorithm)
                ;
        } catch (JWTCreationException exception){
            throw exception;
        }
    }

    public String getSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                .withIssuer("hotel_api")
                .build()
                .verify(token)
                .getSubject();
        } catch (JWTVerificationException exception){
            throw exception;
        }
    }

    public Instant createExpireToken() {
        return LocalDateTime.now().plusHours(12).toInstant(ZoneOffset.of("-03:00"));
    }
}
