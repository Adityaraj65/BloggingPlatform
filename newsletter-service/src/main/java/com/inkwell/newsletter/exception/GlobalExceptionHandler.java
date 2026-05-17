package com.inkwell.newsletter.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handle(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        ex.printStackTrace();

        Map<String, Object> res = new HashMap<>();

        res.put("error", ex.getMessage());

        return ResponseEntity.status(500).body(res);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex
    ) {

        Map<String, Object> res = new HashMap<>();

        String error = ex
                .getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        res.put("error", error);

        return ResponseEntity.badRequest().body(res);
    }
}