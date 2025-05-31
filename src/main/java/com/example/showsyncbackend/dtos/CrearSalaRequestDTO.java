package com.example.showsyncbackend.dtos;

import lombok.Data;

@Data
public class CrearSalaRequestDTO {

    private String nombre;
    private String direccion;
    private Integer capacidad;
    //private String imagen;
    private String descripcion;
    private String ciudad;
    private String provincia;
    private String codigoPostal;


    // Constructor, getters y setters pueden ser generados automáticamente por Lombok
}
