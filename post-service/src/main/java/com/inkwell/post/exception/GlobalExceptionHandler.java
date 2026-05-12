package com.inkwell.post.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    // ================= VALIDATION ERRORS =================

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, Object> res = new HashMap<>();

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
          .getAllErrors()
          .forEach((error) -> {

              String field =
                      ((FieldError) error).getField();

              String message =
                      error.getDefaultMessage();

              errors.put(field, message);
          });

        res.put("error", "Validation failed");

        res.put("fields", errors);

        return ResponseEntity
                .badRequest()
                .body(res);
    }

    // ================= RUNTIME =================

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(
            RuntimeException ex
    ) {

        Map<String, Object> res = new HashMap<>();

        String message = ex.getMessage();

        // CLEAN DATABASE ERRORS

        if (
            message != null &&
            message.contains("Data too long")
        ) {

            message =
                "Image URL is too large";
        }

        res.put("error", message);

        return ResponseEntity
                .badRequest()
                .body(res);
    }

    // ================= GENERIC =================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex
    ) {

        ex.printStackTrace();

        Map<String, Object> res = new HashMap<>();

        res.put(
                "error",
                "Internal server error"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(res);
    }
}