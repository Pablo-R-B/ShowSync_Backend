package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="generos_musicales", schema = "showsync", catalog = "postgres")
public class GenerosMusicales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "generos_musicales")
    private Set<Artistas> artistas = new HashSet<>();

    @ManyToMany(mappedBy = "generos_musicales")
    private Set<Eventos> eventos = new HashSet<>();
}
