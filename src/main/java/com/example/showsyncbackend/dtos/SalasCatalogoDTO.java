package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data

public class SalasCatalogoDTO {

    private String nombre;
    private String direccion;
    private Integer capacidad;
    private String imagen;
    private String descripcion;

    public SalasCatalogoDTO() {
    }

    public SalasCatalogoDTO(String nombre, String direccion, Integer capacidad, String imagen, String descripcion) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.capacidad = capacidad;
        this.imagen = imagen;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SalasCatalogoDTO that = (SalasCatalogoDTO) o;
        return Objects.equals(nombre, that.nombre) && Objects.equals(direccion, that.direccion) && Objects.equals(capacidad, that.capacidad) && Objects.equals(imagen, that.imagen) && Objects.equals(descripcion, that.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre, direccion, capacidad, imagen, descripcion);
    }
}
