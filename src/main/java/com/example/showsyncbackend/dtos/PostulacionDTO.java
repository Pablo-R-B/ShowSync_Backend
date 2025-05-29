package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import com.example.showsyncbackend.enumerados.TipoSolicitud;
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
    private String eventoImagen;
    private String salaNombre;
    private EstadoPostulacion estado;
    private LocalDate fechaPostulacion;
    private LocalDate fechaRespuesta;
    private TipoSolicitud tipoSolicitud;


    public PostulacionDTO(
            Integer id,
            String eventoNombre,
            String artistaNombre,
            String promotorNombre,
            String eventoImagen,
            String salaNombre,
            EstadoPostulacion estado,
            LocalDate fechaPostulacion,
            TipoSolicitud tipoSolicitud
    ) {
        this.id = id;
        this.eventoNombre = eventoNombre;
        this.artistaNombre = artistaNombre;
        this.promotorNombre = promotorNombre;
        this.eventoImagen = eventoImagen;
        this.salaNombre = salaNombre;
        this.estado = estado;
        this.fechaPostulacion = fechaPostulacion;
        this.tipoSolicitud = tipoSolicitud;
    }

}
