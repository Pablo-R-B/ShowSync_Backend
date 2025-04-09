package com.example.showsyncbackend.seguridad.config.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginRequestDTO {
    private String email;

    @JsonProperty("contrasena")
    private String contrasena;
}
