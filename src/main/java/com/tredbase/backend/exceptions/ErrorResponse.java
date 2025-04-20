package com.tredbase.backend.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponse {
    private String error;
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private List<String> details;

    public ErrorResponse(String error, LocalDateTime timestamp, int status, String message, List<String> details) {
        this.error = error;
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.details = details;
    }
}
