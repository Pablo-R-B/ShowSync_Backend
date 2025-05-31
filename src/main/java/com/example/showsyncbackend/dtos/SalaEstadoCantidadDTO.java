package com.example.showsyncbackend.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SalaEstadoCantidadDTO {

    String salaNombre;
    String estado;
    Long cantidad;
}
