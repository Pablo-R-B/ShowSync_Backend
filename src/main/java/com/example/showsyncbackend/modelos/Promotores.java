package com.example.showsyncbackend.modelos;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name="promotores", schema = "showsync", catalog = "postgres")
public class Promotores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="usuario_id", nullable = false, unique = true ,referencedColumnName = "id")
    private Usuario usuario;

    @Column(name="nombre_promotor")
    private String nombrePromotor;

    @Column(name="descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name="imagen_perfil")
    private String imagenPerfil;



}
