package com.example.showsyncbackend.seguridad.config;

import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import com.example.showsyncbackend.seguridad.config.dto.UsuarioRegistroDTO;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.seguridad.config.dto.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthenticationControlador {

    private final AuthenticationService authenticationService;

    private final UsuarioRepositorio usuarioRepositorio;


    public AuthenticationControlador(AuthenticationService authenticationService, UsuarioRepositorio usuarioRepositorio) {
        this.authenticationService = authenticationService;
        this.usuarioRepositorio = usuarioRepositorio;
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

        if (registroDTO.getContrasena() == null || registroDTO.getContrasena().length() < 6 ||
                !registroDTO.getContrasena().matches(".*[A-Z].*") ||
                !registroDTO.getContrasena().matches(".*[0-9].*") ||
                !registroDTO.getContrasena().matches(".*[!@#$%^&*()].*")) {
            return ResponseEntity.badRequest().body("La contraseña debe tener al menos 6 caracteres, incluir al menos una mayúscula, un número y un carácter especial.");
        }

        if (registroDTO.getFechaNacimiento() == null || registroDTO.getFechaNacimiento().isAfter(LocalDate.now()) ||
                registroDTO.getFechaNacimiento().isAfter(LocalDate.now().minusYears(18))) {
            return ResponseEntity.badRequest().body("El usuario debe tener al menos 18 años.");
        }

        if (registroDTO.getRol() == null) {
            return ResponseEntity.badRequest().body("El rol no puede estar vacío.");
        }

        // ✅ Verificar si el email ya existe en la base de datos
        if (usuarioRepositorio.findByEmail(registroDTO.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("El email ya está registrado. Prueba con otro.");
        }

        // ✅ Verificar si el nombre de usuario ya existe en la base de datos
        if (usuarioRepositorio.findByNombreUsuario(registroDTO.getNombreUsuario()).isPresent()) {
            return ResponseEntity.badRequest().body("El nombre de usuario ya está en uso. Prueba con otro.");
        }

        // Convertir el DTO a una entidad Usuario
        Usuario nuevoUsuario = Usuario.builder()
                .nombreUsuario(registroDTO.getNombreUsuario())
                .email(registroDTO.getEmail())
                .contrasena((registroDTO.getContrasena()))
                .fechaNacimiento(registroDTO.getFechaNacimiento())
                .rol(registroDTO.getRol())
                .fechaRegistro(LocalDateTime.now())
                .build();

        // Guardar el usuario en la base de datos
        authenticationService.guardarUsuario(nuevoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado correctamente.");
    }


//    // Método para cifrar la contraseña
//    private String cifrarContrasena(String contrasena) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        return passwordEncoder.encode(contrasena);
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            String token = authenticationService.autenticarUsuario(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getContrasena()
            );
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Credencialeswww incorrectas");
        }
    }










}
