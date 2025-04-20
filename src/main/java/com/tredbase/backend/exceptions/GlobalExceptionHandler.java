package com.tredbase.backend.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
            List<String> errors = ex.getBindingResult()
                    .getFieldErrors()
                    .stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .toList();

            ErrorResponse errorResponse = new ErrorResponse(
                    "Validation failed",
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    HttpStatus.BAD_REQUEST.getReasonPhrase(),
                    errors
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
            ErrorResponse errorResponse = new ErrorResponse(
                    "An unexpected error occurred",
                    LocalDateTime.now(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    List.of(ex.getMessage())
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        @ExceptionHandler(DuplicateException.class)
        public ResponseEntity<?> handleDuplicatePayment(DuplicateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
                    "Duplicate payment, please update reference",
                    LocalDateTime.now(),
                    HttpStatus.CONFLICT.value(),
                    HttpStatus.CONFLICT.getReasonPhrase(),
                    List.of(ex.getMessage())
            ));
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Bad request",
                          LocalDateTime.now(),
                          HttpStatus.BAD_REQUEST.value(),
                          HttpStatus.BAD_REQUEST.getReasonPhrase(),
                          List.of(ex.getMessage())));
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("Resource Not found",
                            LocalDateTime.now(),
                            HttpStatus.NOT_FOUND.value(),
                            HttpStatus.NOT_FOUND.getReasonPhrase(),
                            List.of(ex.getMessage())));
        }
        @ExceptionHandler(InsufficientFundsException.class)
        public ResponseEntity<ErrorResponse> handleNotFoundException(InsufficientFundsException ex) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Payer account balance is insufficient",
                            LocalDateTime.now(),
                            HttpStatus.BAD_REQUEST.value(),
                            HttpStatus.BAD_REQUEST.getReasonPhrase(),
                            List.of(ex.getMessage())));
        }
}
