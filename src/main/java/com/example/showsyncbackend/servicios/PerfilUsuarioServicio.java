package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PerfilUsuarioServicio {

    private final UsuarioRepositorio usuarioRepositorio;

    public PerfilUsuarioServicio(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Map<String, Object> obtenerDatosPerfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No autenticado");
        }

        // Aquí asumimos que el principal son los Claims
        Object principal = auth.getPrincipal();

        if (!(principal instanceof Claims claims)) {
            throw new RuntimeException("No autenticado");
        }

        String emailUsuario = claims.getSubject(); // <- subject contiene normalmente el email o username

        Usuario usuario = usuarioRepositorio.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return Map.of(
                "id", usuario.getId(),
                "email", usuario.getEmail(),
                "nombreUsuario", usuario.getNombreUsuario(),
                "rol", usuario.getRol()
        );
    }

}