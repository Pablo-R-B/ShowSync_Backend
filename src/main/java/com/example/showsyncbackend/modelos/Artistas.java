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
@EqualsAndHashCode
@Table(name="artistas", schema = "showsync", catalog = "postgres")
public class Artistas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="usuario_id", nullable = false, referencedColumnName = "id")
    private Usuario usuario;

    @Column(name="nombre_artista")
    private String nombreArtista;

    @Column(name="biografia")
    private String biografia;

    @Column(name="music_url")
    private String musicUrl;

    @Column(name="imagen_perfil")
    private String imagenPerfil;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "artistas_generos",joinColumns = {@JoinColumn(name="artista_id", nullable = false)},
    inverseJoinColumns = {@JoinColumn(name = "genero_id", nullable = false)})
    private Set<GenerosMusicales> generosMusicales = new HashSet<>(0);

    @ManyToMany(mappedBy = "artistasAsignados")
    @JsonIgnore
    private Set<Eventos> eventos = new HashSet<>();

}
