package com.example.showsyncbackend.modelos;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Este será nuestro chatId

    @Column(name = "id_artista")
    private Long artistaId;

    @Column(name = "id_promotor")
    private Long promotorId;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public Conversacion(Long artistaId, Long promotorId) {
        this.artistaId = artistaId;
        this.promotorId = promotorId;
        this.fechaCreacion = LocalDateTime.now();
    }
}
