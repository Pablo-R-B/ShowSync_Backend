package com.example.showsyncbackend.modelos;

import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = "artistasAsignados")
@Table(name="eventos", schema = "showsync", catalog = "postgres")
public class Eventos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne
    @JoinColumn(name="promotor_id", nullable = false, referencedColumnName = "id")
    private Promotores promotor;

    @Column(name="nombre_evento")
    private String nombre_evento;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_evento")
    private LocalDate fechaEvento;

    @ManyToOne
    @JoinColumn(name="sala_id", nullable = false, referencedColumnName = "id")
    private Salas sala;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private Estado estado;

    @Column(name = "imagen_evento")
    private String imagen_evento;

    @ManyToMany
    @JoinTable(name = "eventos_generos", joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id"))
    private Set<GenerosMusicales> generosMusicales = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "eventos_artistas", joinColumns = @JoinColumn(name = "evento_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id"))

    private Set<Artistas> artistasAsignados = new HashSet<>();


    @Column(name = "seguido")
    private Boolean seguido = false;
}
