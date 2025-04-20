package com.tredbase.backend.util;


import com.tredbase.backend.exceptions.AccessDeniedException;
import com.tredbase.backend.exceptions.NotFoundException;
import com.tredbase.backend.model.User;
import com.tredbase.backend.repository.UserRepository;
import com.tredbase.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;


import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JWTUtil {

    private static final int MINUTES = 3600;
    private static final Logger log = LoggerFactory.getLogger(JWTUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    private static SecretKey secretKey;

    private final UserRepository userRepository;

    public JWTUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        secretKey = Keys.hmacShaKeyFor(decodedKey);
    }




    public String generateToken(String email) {
        // Retrieve the user from the database based on the email
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Get the user's role (you can replace this with the appropriate logic for role retrieval)
            Role role = user.getRole();  // Assuming 'getRole' returns the user's role

            var now = Instant.now();
            return Jwts.builder()
                    .setSubject(email)  // Set the subject (usually the user's email or ID)
                    .setIssuedAt(Date.from(now))  // Set the issued date of the token
                    .setExpiration(Date.from(now.plus(MINUTES, ChronoUnit.MINUTES)))  // Set expiration
                    .claim("role", role)
                    .claim("user_id", user.getId())// Add the role as a claim
                    .signWith(secretKey, SignatureAlgorithm.HS256)  // Sign the token with the secret key and HS256
                    .compact();// Create the compact JWT string
        } else {
            throw new NotFoundException("User with " + email +" not found");
        }
    }

    public static String extractUsername(String token) throws AccessDeniedException {
        return getTokenBody(token).getSubject();
    }

    public static String extractRole(String token) throws AccessDeniedException {
        return getTokenBody(token).get("role", String.class);
    }

    public static Boolean validateToken(String token, UserDetails userDetails) throws AccessDeniedException {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }


    public static Claims getTokenBody(String token) throws AccessDeniedException {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        } catch (JwtException e) {
            throw new AccessDeniedException("Access denied: " + e.getMessage());
        }
    }

    private static boolean isTokenExpired(String token) throws AccessDeniedException {
        Claims claims = getTokenBody(token);
        return claims.getExpiration().before(new Date());
    }
    public String extractUserId(String token) throws AccessDeniedException {
        Claims claims = getTokenBody(token);
        return claims.get("user_id", String.class);
    }
}

