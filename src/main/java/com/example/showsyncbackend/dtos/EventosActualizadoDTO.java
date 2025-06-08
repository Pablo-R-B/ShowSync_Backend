package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.enumerados.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventosActualizadoDTO {
    private Integer id;
    private String nombreEvento; // Coincide con tu HTML
    private String descripcion;
    private LocalDate fechaEvento;
    private Estado estado;
    private String imagenEvento;
    private Integer idSala;
    private String nombreSala;
    private Integer idPromotor;
    private String nombrePromotor;
    private List<GenerosMusicalesDTO> generosMusicales;
    private List<ArtistasDTO> artistasAsignados;
}
