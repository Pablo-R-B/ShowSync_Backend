package com.example.showsyncbackend.modelos;

import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="postulaciones_eventos", schema = "showsync", catalog = "postgres")
public class PostulacionEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Eventos evento;

    @ManyToOne
    @JoinColumn(name = "artista_id", nullable = false)
    private Artistas artista;

    @Column(name = "estado")
    @Enumerated(EnumType.STRING)
    private EstadoPostulacion estadoPostulacion;

    @Column(name = "fecha_postulacion", nullable = false, updatable = false)
    private LocalDateTime fechaPostulacion = LocalDateTime.now();

}
