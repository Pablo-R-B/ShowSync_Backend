package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SalaDTO {
    private Integer id;
    private String nombre;
    private String direccion;
    private Integer capacidad;
    private String imagen;
    private String descripcion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;
    private boolean suspendida; // Tipo primitivo (no puede ser null)


    public SalaDTO() {

    }
}

