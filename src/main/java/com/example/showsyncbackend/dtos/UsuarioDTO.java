package com.example.showsyncbackend.dtos;


import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
public class UsuarioDTO {
    private Integer id;
    private String nombreUsuario;
    private String email;
    private Rol rol;
    private boolean verificado;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;







    public UsuarioDTO(UsuarioDTO usuarioDTO) {
    }

    public UsuarioDTO(Integer id, String nombreUsuario, String email) {
    }
}