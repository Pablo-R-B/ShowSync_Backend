package com.example.showsyncbackend.seguridad.config.Controllers;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import com.example.showsyncbackend.seguridad.config.dto.PerfilDTO;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/auth/account")
@RequiredArgsConstructor
public class AccountControlador {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder passwordEncoder;

    @PutMapping("/update")
    public ResponseEntity<Map<String, String>> actualizarPerfil(@RequestBody PerfilDTO perfilDTO) {
        // Extraer los Claims directamente del contexto
        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = claims.getSubject(); // asumimos que el subject del token es el email

        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar y actualizar email
        if (perfilDTO.getNuevoEmail() != null && !perfilDTO.getNuevoEmail().isEmpty()
                && !perfilDTO.getNuevoEmail().equals(usuario.getEmail())) {

            if (usuarioRepositorio.existsByEmail(perfilDTO.getNuevoEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "El email ya está en uso."));
            }
            usuario.setEmail(perfilDTO.getNuevoEmail());
        }

        // Validar y actualizar nombre de usuario
        if (perfilDTO.getNuevoNombreUsuario() != null && !perfilDTO.getNuevoNombreUsuario().isEmpty()
                && !perfilDTO.getNuevoNombreUsuario().equals(usuario.getNombreUsuario())) {

            if (usuarioRepositorio.existsByNombreUsuario(perfilDTO.getNuevoNombreUsuario())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("message", "El nombre de usuario ya está en uso."));
            }
            usuario.setNombreUsuario(perfilDTO.getNuevoNombreUsuario());
        }

        // Actualizar contraseña si viene
        if (perfilDTO.getNuevaContrasena() != null && !perfilDTO.getNuevaContrasena().isEmpty()) {
            usuario.setContrasena(passwordEncoder.encode(perfilDTO.getNuevaContrasena()));
        }

        usuarioRepositorio.save(usuario);
        return ResponseEntity.ok(Map.of("message", "Perfil actualizado correctamente"));
    }

}
