package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.UsuarioRegistroDTO;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.servicios.UsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    private final UsuarioServicio usuarioServicio;

    public UsuarioControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody UsuarioRegistroDTO registroDTO) {
        if (!registroDTO.esValido()) {
            return ResponseEntity.badRequest().body("Los datos proporcionados no son válidos.");
        }

        // Convertir el DTO a una entidad Usuario
        Usuario nuevoUsuario = Usuario.builder()
                .nombreUsuario(registroDTO.getNombreUsuario())
                .email(registroDTO.getEmail())
                .contrasenya(registroDTO.getContrasenya())  // Aquí deberías cifrar la contraseña
                .fechaNacimiento(registroDTO.getFechaNacimiento())
                .rol(registroDTO.getRol())
                .fechaRegistro(LocalDate.now())
                .build();

        // Llamar al servicio para guardar el usuario
        usuarioServicio.guardarUsuario(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente.");
    }









}
