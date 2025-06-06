package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name="eventos_artistas ", schema = "showsync", catalog = "postgres")
public class EventosArtistas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="evento_id", nullable = false)
    private Integer eventoId;

    @Column(name="artista_id", nullable = false)
    private Integer artistaId;
}
