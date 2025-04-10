package com.example.showsyncbackend.seguridad.config.services;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmailService emailService;



    @PostConstruct
    public void init() {
        System.out.println("\n==== CONFIGURACIÓN DE SEGURIDAD ====");
        System.out.println("Tipo de PasswordEncoder: " + passwordEncoder.getClass().getName());
        System.out.println("Hash de ejemplo ('test123'): " + passwordEncoder.encode("test123"));
        System.out.println("Hash de tu contraseña ('Contrasena123!'): " + passwordEncoder.encode("Contrasena123!"));
        System.out.println("====================================\n");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepositorio.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + username));
    }

    public void guardarUsuario(Usuario usuario) {
        System.out.println("\n==== REGISTRO DE USUARIO ====");
        System.out.println("Contraseña original: " + usuario.getContrasena());

        // Encriptar la contraseña solo una vez
        String encodedPassword = passwordEncoder.encode(usuario.getContrasena());
        System.out.println("Contraseña encriptada: " + encodedPassword);
        usuario.setContrasena(encodedPassword);

        // Generar y asignar token de verificación
        String token = UUID.randomUUID().toString();
        usuario.setVerificacionToken(token);
        usuario.setVerificado(false); // Asegurar que el usuario no esté verificado inicialmente

        // Guardar usuario
        usuarioRepositorio.save(usuario);

        System.out.println("Usuario registrado con email: " + usuario.getEmail());
        System.out.println("=============================\n");

        // Enviar correo de verificación
        emailService.enviarCorreoVerificacion(usuario.getEmail(), token);
    }

    public String autenticarUsuario(String email, String contrasena) {
        System.out.println("\n==== INTENTO DE AUTENTICACIÓN ====");
        System.out.println("Email recibido: " + email);

        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado en la base de datos");
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        // 1. Verificar si el email está verificado
        if (!usuario.isVerificado()) {
            throw new RuntimeException("Por favor verifica tu email antes de iniciar sesión");
        }

        // 2. Verificar contraseña
        System.out.println("Comparando contraseña para: " + email);
        if (!passwordEncoder.matches(contrasena, usuario.getContrasena())) {
            System.out.println("--- FALLA DE AUTENTICACIÓN ---");
            System.out.println("Hash almacenado en BBDD: " + usuario.getContrasena());
            System.out.println("Hash generado ahora: " + passwordEncoder.encode(contrasena));
            throw new RuntimeException("Credenciales incorrectas");
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (passwordEncoder.matches(contrasenya, usuario.getContrasenya())) {
            return jwtService.generateToken(usuario);
        } else {
            throw new RuntimeException("Credenciales incorrectas. AuthService");

        }

        System.out.println("Autenticación exitosa para: " + email);
        System.out.println("===============================\n");

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