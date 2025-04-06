package com.example.showsyncbackend.seguridad.config.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String contrasenya;
}
