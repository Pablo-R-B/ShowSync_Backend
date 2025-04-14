package com.example.showsyncbackend.modelos;

import com.example.showsyncbackend.enumerados.Rol;
import jakarta.persistence.*;
import lombok.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="usuarios", schema = "showsync", catalog = "postgres")
public class Usuario  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "contraseña", nullable = false)
    private String contrasenya;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name="rol", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Rol rol;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

}
