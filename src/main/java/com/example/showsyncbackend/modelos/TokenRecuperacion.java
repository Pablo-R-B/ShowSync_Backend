package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tokens_recuperacion", schema = "showsync", catalog = "postgres")
public class TokenRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token", nullable = false)
    private String token;

    // Getter y Setter para expiracion (si es necesario)
    @Setter
    @Getter
    @Column(name = "expiracion", nullable = false)
    private LocalDateTime expiracion;

    // Relación con Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

}
