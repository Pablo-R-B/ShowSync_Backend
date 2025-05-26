package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PostulacionDTO {
    private Integer id;
    private String eventoNombre;
    private String artistaNombre;
    private String promotorNombre;
    private EstadoPostulacion estado;
    private LocalDate fechaPostulacion;
    private LocalDate fechaRespuesta;


    public PostulacionDTO(
            Integer id,
            String eventoNombre,
            String artistaNombre,
            String promotorNombre,
            EstadoPostulacion estado,
            LocalDate fechaPostulacion
    ) {
        this.id = id;
        this.eventoNombre = eventoNombre;
        this.artistaNombre = artistaNombre;
        this.promotorNombre = promotorNombre;
        this.estado = estado;
        this.fechaPostulacion = fechaPostulacion;
    }

}
