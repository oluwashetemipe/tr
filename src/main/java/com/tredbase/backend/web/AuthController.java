package com.tredbase.backend.web;

import com.tredbase.backend.dto.LoginRequest;
import com.tredbase.backend.dto.LoginResponse;
import com.tredbase.backend.exceptions.AccessDeniedException;
import com.tredbase.backend.exceptions.NotFoundException;
import com.tredbase.backend.model.User;
import com.tredbase.backend.repository.UserRepository;
import com.tredbase.backend.service.AuthService;
import com.tredbase.backend.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.tredbase.backend.util.JWTUtil.getTokenBody;

@RestController
@RequestMapping("v1/api/auth")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }



    @PostMapping(value = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(authService.login(request));
    }
}
