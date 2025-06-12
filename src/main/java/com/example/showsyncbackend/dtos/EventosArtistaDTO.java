package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventosArtistaDTO {
    private String nombreEvento;
    private LocalDate fechaEvento;
    private String nombreSala;
}
