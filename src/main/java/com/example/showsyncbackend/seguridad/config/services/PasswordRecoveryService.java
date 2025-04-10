package com.example.showsyncbackend.seguridad.config.services;

import com.example.showsyncbackend.modelos.TokenRecuperacion;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.TokenRecuperacionRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
@Service
@RequiredArgsConstructor // Usa esta anotación en lugar de @Autowired
public class PasswordRecoveryService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final TokenRecuperacionRepositorio tokenRecuperacionRepositorio;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder; // Inyecta el PasswordEncoder

    public String generarTokenRecuperacion(String email) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Elimina tokens antiguos para este usuario
        tokenRecuperacionRepositorio.deleteByUsuarioEmail(email);

        String token = UUID.randomUUID().toString();
        TokenRecuperacion tokenRecuperacion = TokenRecuperacion.builder()
                .token(token)
                .usuario(usuario)
                .expiracion(LocalDateTime.now().plusHours(1))
                .build();

        tokenRecuperacionRepositorio.save(tokenRecuperacion);
        emailService.enviarCorreoRecuperacion(email, token);

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

    @Transactional
    public void restablecerContrasena(Usuario usuario, String nuevaContrasena) {
        // Usa el passwordEncoder inyectado
        usuario.setContrasena(passwordEncoder.encode(nuevaContrasena));
        usuarioRepositorio.save(usuario);

        // Elimina el token después de usarlo
        tokenRecuperacionRepositorio.deleteByUsuarioEmail(usuario.getEmail());
    }
}
