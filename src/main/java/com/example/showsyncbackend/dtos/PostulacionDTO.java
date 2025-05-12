package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostulacionDTO {
    private Integer id;
    private String eventoNombre;
    private EstadoPostulacion estado;
    private LocalDateTime fechaPostulacion;
}
