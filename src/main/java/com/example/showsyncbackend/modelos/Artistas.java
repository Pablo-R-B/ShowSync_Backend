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
@EqualsAndHashCode(exclude = {"generosMusicales"})
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

    @ManyToMany
    @JoinTable(name = "artistas_generos",joinColumns = {@JoinColumn(name="artista_id", nullable = false)},
    inverseJoinColumns = {@JoinColumn(name = "evento_id", nullable = false)})
    private Set<GenerosMusicales> generosMusicales = new HashSet<>(0);
}
