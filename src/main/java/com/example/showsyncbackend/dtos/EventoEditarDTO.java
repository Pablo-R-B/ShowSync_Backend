package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventoEditarDTO {
    private Integer id;
    private String nombreEvento;
    private String descripcion;
    private Integer idSala;
    private String estado;
    private String imagenEvento;
    private String estadoPublicacion;
    private Set<ArtistasDTO> artistasAsignados;
}