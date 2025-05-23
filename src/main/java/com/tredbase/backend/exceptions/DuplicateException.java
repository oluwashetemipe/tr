package com.tredbase.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class DuplicateException extends RuntimeException{
    public DuplicateException(String message) {
        super(message);
    }
    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

}
