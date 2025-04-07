package com.example.showsyncbackend.seguridad.config.services;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final JWTService jwtService;
    private final EmailService emailService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    // Método para registrar un nuevo usuario
    public void guardarUsuario(Usuario usuario) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(usuario.getContrasenya());
        usuario.setContrasenya(encodedPassword);

        // Generar un token de verificación único
        String token = UUID.randomUUID().toString();
        usuario.setVerificacionToken(token); // Usamos verificacionToken

        // Guardar usuario en la base de datos
        usuarioRepositorio.save(usuario);

        // Enviar correo de verificación
        emailService.enviarCorreoVerificacion(usuario.getEmail(), token);
    }

    // Método para autenticar al usuario y generar el JWT
    public String autenticarUsuario(String email, String contrasenya) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        if (!usuario.isVerificado()) {
            throw new RuntimeException("El email aún no ha sido verificado.");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(contrasenya, usuario.getContrasenya())) {
            return jwtService.generateToken(usuario);
        } else {
            throw new RuntimeException("Credenciales incorrectas. AuthService");

        }


    }

    // Método para verificar el email del usuario
    public boolean verificarEmail(String token) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByVerificacionToken(token);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setVerificado(true); // Marcamos al usuario como verificado
            usuario.setVerificacionToken(null); // Limpiamos el token de verificación
            usuarioRepositorio.save(usuario);
            return true;
        }
        return false;
    }
}
