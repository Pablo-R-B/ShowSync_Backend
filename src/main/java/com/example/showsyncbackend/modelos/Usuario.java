package com.example.showsyncbackend.modelos;

import com.example.showsyncbackend.enumerados.Rol;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Solo para auditoría
@Table(name="usuarios", schema = "showsync", catalog = "postgres")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre_usuario", nullable = false)
    private String nombreUsuario;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name="rol", nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private Rol rol;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro ;

    @Column(name = "verificado", nullable = false)
    private boolean verificado = false;

    @Column(name = "verificacion_token", nullable = true)
    private String verificacionToken;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devolvemos el rol del usuario como una autoridad
        return Collections.singletonList(new SimpleGrantedAuthority(rol.name()));
    }

    @Override
    public String getPassword() {
        // Devolvemos la contraseña del usuario
        return this.contrasena;

    }

    @Override
    public String getUsername() {
        // Devolvemos el email como el nombre de usuario
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Retorna true si la cuenta no ha expirado
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Retorna true si la cuenta no está bloqueada
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Retorna true si las credenciales no han expirado
        return true;
    }

    @Override
    public boolean isEnabled() {
        return verificado;
    }

    public void setContrasenya(String contrasenya) {
        this.contrasena = new BCryptPasswordEncoder().encode(contrasenya);
    }

    public void verifyEmail(String token) {
        if (this.verificacionToken.equals(token)) {
            this.verificado = true;
        } else {
            throw new IllegalArgumentException("Invalid verification token.");
        }
    }
}
