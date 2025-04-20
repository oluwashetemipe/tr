package com.tredbase.backend.service;


import com.tredbase.backend.exceptions.NotFoundException;
import com.tredbase.backend.model.User;
import com.tredbase.backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepository repository;

    public UserDetailsServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = null;
        try {
            user = repository.findByEmail(email).orElseThrow(() ->
                    new NotFoundException(String.format("User does not exist, email: %s", email)));
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

            log.info("User with role {}", user.getRole());
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities(authorities)
                    .build();
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
