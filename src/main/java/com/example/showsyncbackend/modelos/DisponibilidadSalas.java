package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "disponibilidad_salas", schema = "showsync", catalog = "postgres", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"sala_id", "fecha"})
})
public class DisponibilidadSalas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "sala_id", nullable = false)
    private Salas sala;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "disponibilidad")
    private Boolean disponibilidad;

}
