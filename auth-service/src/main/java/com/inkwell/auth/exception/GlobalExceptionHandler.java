package com.inkwell.auth.exception;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handle(RuntimeException ex) {

        Map<String, Object> res = new HashMap<>();
        res.put("error", ex.getMessage());

        return ResponseEntity.badRequest().body(res);
    }
}