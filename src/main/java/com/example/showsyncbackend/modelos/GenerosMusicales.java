package com.example.showsyncbackend.modelos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "artistas")
@ToString(exclude = "artistas")
@Table(name="generos_musicales", schema = "showsync", catalog = "postgres")
public class GenerosMusicales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "generosMusicales")
    private Set<Artistas> artistas = new HashSet<>();

    @ManyToMany(mappedBy = "generosMusicales")
    private Set<Eventos> eventos = new HashSet<>();

    public GenerosMusicales(Long id, String nombre) {
        this.id = id.intValue();
        this.nombre = nombre;
    }

}
