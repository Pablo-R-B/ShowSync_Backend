package com.example.showsyncbackend.dtos;


import com.example.showsyncbackend.modelos.Salas;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DisponibilidadSalaDTO {

    private Integer id;
    private LocalDate fecha;
    private Boolean disponibilidad;
    private SalaDTO sala;



}