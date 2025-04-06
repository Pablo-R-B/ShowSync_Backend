package com.example.showsyncbackend.seguridad.config;

import com.example.showsyncbackend.dtos.UsuarioRegistroDTO;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.seguridad.config.dto.LoginRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
public class AuthenticationControlador {

    private final AuthenticationService authenticationService;

    public AuthenticationControlador(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
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
        authenticationService.guardarUsuario(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authenticationService.autenticarUsuario(loginRequest.getEmail(), loginRequest.getContrasenya());
            return ResponseEntity.ok(token); // Enviar el token JWT como respuesta
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credencialeswww incorrectas");
        }
    }









}
