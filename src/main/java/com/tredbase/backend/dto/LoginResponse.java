package com.tredbase.backend.dto;


import org.springframework.http.HttpStatusCode;

public record LoginResponse(String email,
                            String access_token,
                            String token_type,
                            java.util.Date expires_in) {
}
