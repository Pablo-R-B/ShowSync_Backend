package com.example.showsyncbackend.seguridad.config.manejoErrores;

import lombok.Getter;

@Getter
public class CustomAuthenticationException extends RuntimeException {
    private final int statusCode;

    public CustomAuthenticationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

}
