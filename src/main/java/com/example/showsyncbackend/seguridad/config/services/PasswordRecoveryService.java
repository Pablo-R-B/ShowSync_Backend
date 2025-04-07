package com.example.showsyncbackend.seguridad.config.services;

import com.example.showsyncbackend.modelos.TokenRecuperacion;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.TokenRecuperacionRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
public class PasswordRecoveryService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private TokenRecuperacionRepositorio tokenRecuperacionRepositorio;

    @Autowired
    private EmailService emailService;

    public String generarTokenRecuperacion(String email) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = UUID.randomUUID().toString(); // Genera un token único

        // Guardar el token y la fecha de expiración
        TokenRecuperacion tokenRecuperacion = new TokenRecuperacion();
        tokenRecuperacion.setToken(token);
        tokenRecuperacion.setUsuario(usuario);
        tokenRecuperacion.setExpiracion(LocalDateTime.now().plusHours(1)); // Expira en 1 hora

        tokenRecuperacionRepositorio.save(tokenRecuperacion);

        // Enviar correo con el enlace de recuperación
        emailService.enviarCorreoRecuperacion(usuario.getEmail(), token);

        return token;
    }

    public TokenRecuperacion validarToken(String token) {
        TokenRecuperacion tokenRecuperacion = tokenRecuperacionRepositorio.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token no válido"));

        if (tokenRecuperacion.getExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado");
        }

        return tokenRecuperacion;
    }

    public void restablecerContrasena(Usuario usuario, String nuevaContrasena) {
        // Asegurarnos de no modificar el token de verificación o el estado de verificación
        String contraseñaEncriptada = new BCryptPasswordEncoder().encode(nuevaContrasena);
        usuario.setContrasenya(contraseñaEncriptada);

        // No cambiar el verificado ni el token de verificación
        usuarioRepositorio.save(usuario);
    }
}
