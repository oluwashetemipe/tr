package com.tredbase.backend.service;

import com.tredbase.backend.dto.LoginRequest;
import com.tredbase.backend.dto.LoginResponse;
import com.tredbase.backend.exceptions.AccessDeniedException;
import com.tredbase.backend.exceptions.NotFoundException;
import com.tredbase.backend.model.User;
import com.tredbase.backend.repository.UserRepository;
import com.tredbase.backend.util.JWTUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tredbase.backend.util.JWTUtil.getTokenBody;

@Service
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public LoginResponse login(LoginRequest request) {
        Optional<User> userOptional =userRepository.findByEmail(request.email());
        User user = userOptional.orElseThrow(() -> new NotFoundException("User with " + request.email() + " not found"));
        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        log.info("Logged in with email: {}", request.email());

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        authenticationManager.authenticate(authentication);
        String token = jwtUtil.generateToken(request.email());
        Claims claims;
        try {
             claims = getTokenBody(token);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        String tokenType = "Bearer";
        return new LoginResponse(request.email(), token,tokenType, claims.getExpiration());
    }
}
