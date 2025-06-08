package com.example.showsyncbackend.seguridad.config.services;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import com.example.showsyncbackend.seguridad.config.manejoErrores.CustomAuthenticationException;
import com.example.showsyncbackend.servicios.UsuarioServicio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UsuarioServicio usuarioServicio;

    @PostConstruct
    public void init() {

    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }

    public void guardarUsuario(Usuario usuario) {


        // Encriptar la contraseña solo una vez
        String encodedPassword = passwordEncoder.encode(usuario.getContrasena());
        usuario.setContrasena(encodedPassword);

        // Generar y asignar token de verificación
        String token = UUID.randomUUID().toString();
        usuario.setVerificacionToken(token);
        usuario.setVerificado(false); // Asegurar que el usuario no esté verificado inicialmente

        // Guardar usuario
        usuarioRepositorio.save(usuario);


        // Enviar correo de verificación
        emailService.enviarCorreoVerificacion(usuario.getEmail(), token);
    }

    public String autenticarUsuario(String email, String contrasena) {
        Usuario usuario = usuarioServicio.getUsuarioByEmail(email);

        // 1. Verificar si el email está verificado
        if (!usuario.isVerificado()) {
            throw new CustomAuthenticationException("Por favor verifica tu email antes de iniciar sesión", 403);
        }

        // 2. Verificar contraseña
        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            throw new CustomAuthenticationException("Credenciales incorrectas", 401);
        }

        return jwtService.generateToken(usuario);
    }

    public boolean verificarEmail(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByVerificacionToken(token);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setVerificado(true);
            usuario.setVerificacionToken(null); // Limpiar token después de verificación
            usuarioRepositorio.save(usuario);
            return true;
        }
        return false;
    }
}