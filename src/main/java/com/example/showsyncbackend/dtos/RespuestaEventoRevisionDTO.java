package com.example.showsyncbackend.dtos;


import com.example.showsyncbackend.enumerados.Estado;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class RespuestaEventoRevisionDTO {
    private Integer id;
    private String nombreEvento;
    private String descripcion;
    private LocalDate fechaEvento;
    private Estado estado;
    private String imagenEvento;
    private Integer idSala;
    private String nombreSala;
    private String nombrePromotor;
    private Set<String> generosMusicales;
}
