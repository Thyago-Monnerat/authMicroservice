package com.authMS.Auth.microsservice.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.exceptions.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class JwtService {

    @Value("${jwt.key}")
    private String key;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateToken(UserDto userDto) {
        Algorithm algorithm = Algorithm.HMAC256(key);

        try {
            return JWT.create()
                    .withIssuer(issuer)
                    .withSubject(userDto.email())
                    .withClaim("username", userDto.username())
                    .withExpiresAt(generateExpirationDate(120))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new JwtException("Jwt creation exception " + e.getMessage());
        }
    }

    public String validateToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(key);
        try {
            return JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException e) {
            throw new JwtException("Jwt validation exception " + e.getMessage());
        }
    }

    public String getTokenJti(String token){
        return JWT.decode(token).getId();
    }

    private Instant generateExpirationDate(Integer minutes) {
        return LocalDateTime.now().plusMinutes(minutes).toInstant(ZoneOffset.of("-03:00"));
    }
}
