package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="salas", schema = "showsync", catalog = "postgres")
public class Salas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "direccion")
    private String direccion;

    @Column(name = "capacidad")
    private Integer capacidad;

    @Column(name="imagen")
    private String imagen;

    @ManyToOne
    @JoinColumn(name = "administrador_id", nullable = false)
    private Usuario administrador;

    @Column(name="descripcion")
    private String descripcion;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Usuario getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Usuario administrador) {
        this.administrador = administrador;
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
        Salas salas = (Salas) o;
        return Objects.equals(id, salas.id) && Objects.equals(nombre, salas.nombre) && Objects.equals(direccion, salas.direccion) && Objects.equals(capacidad, salas.capacidad) && Objects.equals(imagen, salas.imagen) && Objects.equals(administrador, salas.administrador) && Objects.equals(descripcion, salas.descripcion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, direccion, capacidad, imagen, administrador, descripcion);
    }
}
