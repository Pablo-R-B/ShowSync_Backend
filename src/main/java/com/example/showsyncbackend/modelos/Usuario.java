package com.example.showsyncbackend.modelos;

import com.example.showsyncbackend.enumerados.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios", schema = "showsync", catalog = "postgres")
public class Usuario implements UserDetails {

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
    private LocalDate fechaRegistro = LocalDate.now();

    @Column(name = "verificado", nullable = false)
    private boolean verificado = false;

    @Column(name = "verificacion_token", nullable = true)
    private String verificacionToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        return this.contrasenya;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verificado;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasenya = new BCryptPasswordEncoder().encode(contrasenya);
    }

    public void verifyEmail(String token) {
        if (this.verificacionToken.equals(token)) {
            this.verificado = true;
        } else {
            throw new IllegalArgumentException("Invalid verification token.");
        }
    }
}
