package com.inkwell.auth.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ================= VALIDATION ERRORS =================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, Object> response =
                new HashMap<>();

        String message =
                ex.getBindingResult()
                  .getFieldError()
                  .getDefaultMessage();

        response.put("error", message);

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // ================= RUNTIME ERRORS =================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(
            RuntimeException ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "error",
                ex.getMessage()
        );

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        // IMPORTANT

        if (
            ex.getMessage().equals("User not found")
        ) {

            return ResponseEntity
                    .status(404)
                    .body(response);
        }

        if (
            ex.getMessage().equals("Invalid credentials")
        ) {

            return ResponseEntity
                    .status(401)
                    .body(response);
        }

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // ================= GENERIC ERRORS =================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex) {

        Map<String, Object> response =
                new HashMap<>();

        response.put(
                "error",
                "Internal server error"
        );

        response.put(
                "timestamp",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(500)
                .body(response);
    }
}