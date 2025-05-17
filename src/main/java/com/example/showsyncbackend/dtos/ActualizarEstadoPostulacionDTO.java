package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ActualizarEstadoPostulacionDTO {
    private EstadoPostulacion nuevoEstado;
}
