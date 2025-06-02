package com.example.showsyncbackend.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RespuestaContactoDTO {
    private boolean success;
    private String message;
}