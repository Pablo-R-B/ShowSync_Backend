package com.example.showsyncbackend.seguridad.config.dto;

import com.example.showsyncbackend.enumerados.Rol;
import lombok.Data;

import java.time.LocalDate;


@Data
public class UsuarioRegistroDTO {
    private String nombreUsuario;
    private String email;
    private String contrasena;
    private LocalDate fechaNacimiento;
    private Rol rol;




}
