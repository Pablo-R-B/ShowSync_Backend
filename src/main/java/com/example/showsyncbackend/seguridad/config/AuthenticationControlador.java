package com.example.showsyncbackend.seguridad.config;

import com.example.showsyncbackend.seguridad.config.dto.UsuarioRegistroDTO;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.seguridad.config.dto.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        // Validación personalizada
        if (registroDTO.getNombreUsuario() == null || registroDTO.getNombreUsuario().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre de usuario no puede estar vacío.");
        }

        if (registroDTO.getEmail() == null || registroDTO.getEmail().trim().isEmpty() || !registroDTO.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body("El email debe ser válido.");
        }

        if (registroDTO.getContrasenya() == null || registroDTO.getContrasenya().length() < 6 || !registroDTO.getContrasenya().matches(".*[A-Z].*") || !registroDTO.getContrasenya().matches(".*[0-9].*") || !registroDTO.getContrasenya().matches(".*[!@#$%^&*()].*")) {
            return ResponseEntity.badRequest().body("La contraseña debe tener al menos 6 caracteres, incluir al menos una mayúscula, un número y un carácter especial.");
        }

        if (registroDTO.getFechaNacimiento() == null || registroDTO.getFechaNacimiento().isAfter(LocalDate.now()) || registroDTO.getFechaNacimiento().isAfter(LocalDate.now().minusYears(18))) {
            return ResponseEntity.badRequest().body("El usuario debe tener al menos 18 años.");
        }

        if (registroDTO.getRol() == null) {
            return ResponseEntity.badRequest().body("El rol no puede estar vacío.");
        }

        // Convertir el DTO a una entidad Usuario
        Usuario nuevoUsuario = Usuario.builder()
                .nombreUsuario(registroDTO.getNombreUsuario())
                .email(registroDTO.getEmail())
                .contrasenya(cifrarContrasenya(registroDTO.getContrasenya()))  // Cifrar la contraseña
                .fechaNacimiento(registroDTO.getFechaNacimiento())
                .rol(registroDTO.getRol())
                .fechaRegistro(LocalDate.now())
                .build();

        // Llamar al servicio para guardar el usuario
        authenticationService.guardarUsuario(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente.");
    }

    // Método para cifrar la contraseña
    private String cifrarContrasenya(String contrasenya) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(contrasenya);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            String token = authenticationService.autenticarUsuario(loginRequestDTO.getEmail(), loginRequestDTO.getContrasenya());
            return ResponseEntity.ok(token); // Enviar el token JWT como respuesta
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credencialeswww incorrectas");
        }
    }









}
