package com.example.showsyncbackend.seguridad.config.services;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        System.out.println("\n==== CONFIGURACIÓN DE SEGURIDAD ====");
        System.out.println("Tipo de PasswordEncoder: " + passwordEncoder.getClass().getName());
        System.out.println("Hash de ejemplo ('test123'): " + passwordEncoder.encode("test123"));
        System.out.println("Hash de tu contraseña ('Contrasena123!'): " + passwordEncoder.encode("Contrasena123!"));
        System.out.println("====================================\n");
    }
    private final JWTService jwtService;
    private final EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    // Método para registrar un nuevo usuario
    public void guardarUsuario(Usuario usuario) {
        System.out.println("\n==== REGISTRO DE USUARIO ====");
        System.out.println("Contraseña original: " + usuario.getContrasena());

        String encodedPassword = passwordEncoder.encode(usuario.getContrasena());
        System.out.println("Contraseña encriptada: " + encodedPassword);

        usuario.setContrasena(encodedPassword);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(usuario.getContrasenya());
        usuario.setContrasenya(encodedPassword);

        // Generar un token de verificación único
        String token = UUID.randomUUID().toString();
        usuario.setVerificacionToken(token); // Usamos verificacionToken

        // Guardar usuario en la base de datos
        usuarioRepositorio.save(usuario);

        System.out.println("Usuario registrado con email: " + usuario.getEmail());
        System.out.println("=============================\n");

        // Enviar correo de verificación
        emailService.enviarCorreoVerificacion(usuario.getEmail(), token);
    }

    public String autenticarUsuario(String email, String contrasena) {
        System.out.println("\n==== INTENTO DE AUTENTICACIÓN ====");
        System.out.println("Email recibido: " + email);
        System.out.println("Contraseña recibida (texto plano): " + contrasena);

        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado en la base de datos");
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        System.out.println("Hash almacenado en BBDD: " + usuario.getContrasena());
        System.out.println("Longitud del hash: " + usuario.getContrasena().length());

        boolean match = passwordEncoder.matches(contrasena, usuario.getContrasena());
        System.out.println("Resultado de comparación BCrypt: " + match);
        if (!usuario.isVerificado()) {
            throw new RuntimeException("El email aún no ha sido verificado.");
        }

        if (!match) {
            System.out.println("\n--- DEBUG DE CONTRASEÑA ---");
            System.out.println("Posibles problemas:");
            System.out.println("1. Contraseña con espacios: '" + contrasena.trim() + "'");
            System.out.println("2. Sin caracter especial: '" + contrasena.replace("!", "") + "'");
            System.out.println("3. Hash generado ahora: " + passwordEncoder.encode(contrasena));

            // Verificación manual del hash
            System.out.println("\nVerificación manual:");
            System.out.println("¿Coincide con trim? " + passwordEncoder.matches(contrasena.trim(), usuario.getContrasena()));
            System.out.println("¿Coincide sin !? " + passwordEncoder.matches(contrasena.replace("!", ""), usuario.getContrasena()));

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(contrasenya, usuario.getContrasenya())) {
            return jwtService.generateToken(usuario);
        } else {
            throw new RuntimeException("Credenciales incorrectas");
        }

        System.out.println("Autenticación exitosa para: " + email);
        System.out.println("===============================\n");
        return jwtService.generateToken(usuario);
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
