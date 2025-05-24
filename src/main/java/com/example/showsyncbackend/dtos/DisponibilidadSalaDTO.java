package com.example.showsyncbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DisponibilidadSalaDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    private Boolean disponibilidad;
    private Integer salaId;
    private Integer eventoId; // Puede ser null si ese día no hay evento
    private String estadoEvento; // NUEVO: estado del evento si hay evento asignado
}
