package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Entity
@Table(name = "conversaciones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // ← Añade esta anotación
public class Conversacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Este será nuestro chatId

    @Column(name = "id_artista")
    private Long artistaId;

    @Column(name = "id_promotor")
    private Long promotorId;

    @CreatedDate // ← Añade esta anotación
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // Puedes mantener este constructor pero quitar la asignación de fecha
    public Conversacion(Long artistaId, Long promotorId) {
        this.artistaId = artistaId;
        this.promotorId = promotorId;
        // Spring ahora manejará la fecha automáticamente
    }
}