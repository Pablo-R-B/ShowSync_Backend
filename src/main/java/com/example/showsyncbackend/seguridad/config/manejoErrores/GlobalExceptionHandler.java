package com.example.showsyncbackend.seguridad.config.manejoErrores;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomAuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleCustomAuthenticationException(CustomAuthenticationException ex) {
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of(
                "mensaje", ex.getMessage(),
                "codigo", ex.getStatusCode()
        ));
    }


}
