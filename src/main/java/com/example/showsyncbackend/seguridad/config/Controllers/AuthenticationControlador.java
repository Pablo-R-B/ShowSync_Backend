package com.example.showsyncbackend.seguridad.config.Controllers;

import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import com.example.showsyncbackend.seguridad.config.dto.UsuarioRegistroDTO;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.seguridad.config.dto.LoginRequestDTO;
import com.example.showsyncbackend.seguridad.config.services.AuthenticationService;
import com.example.showsyncbackend.servicios.PerfilUsuarioServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthenticationControlador {

    private final AuthenticationService authenticationService;

    private final UsuarioRepositorio usuarioRepositorio;

    private static final Logger log = LoggerFactory.getLogger(AuthenticationControlador.class);

    private final PerfilUsuarioServicio perfilUsuarioServicio;

    public AuthenticationControlador(AuthenticationService authenticationService, UsuarioRepositorio usuarioRepositorio, PerfilUsuarioServicio perfilUsuarioServicio) {
        this.authenticationService = authenticationService;
        this.usuarioRepositorio = usuarioRepositorio;
        this.perfilUsuarioServicio = perfilUsuarioServicio;
    }

    @PostMapping("/registro")
    public ResponseEntity<String> registrarUsuario(@RequestBody UsuarioRegistroDTO registroDTO) {
        // Validación personalizada
        if (registroDTO.getNombreUsuario() == null || registroDTO.getNombreUsuario().trim().isEmpty() || registroDTO.getNombreUsuario().contains(" ") || registroDTO.getNombreUsuario().matches(".*[!¡?¿+*^<>/ªº·#`;:'€~%,=()@¬|].*")) {
            return ResponseEntity.badRequest().body("El nombre de usuario no puede estar vacío, contener espacios ni incluir caracteres especiales como: !¡?¿+*^<>/ªº·#`;:'€~%,=()@¬|");
        }

        if (registroDTO.getEmail() == null || registroDTO.getEmail().trim().isEmpty() || !registroDTO.getEmail().contains("@")) {
            return ResponseEntity.badRequest().body("El email debe ser válido.");
        }

        if (registroDTO.getContrasena() == null || registroDTO.getContrasena().length() < 6 ||
                !registroDTO.getContrasena().matches(".*[A-Z].*") ||
                !registroDTO.getContrasena().matches(".*[0-9].*") ||
                !registroDTO.getContrasena().matches(".*[!@#$%^&*._()-?¿¡+<>/].*")) {
            return ResponseEntity.badRequest().body("La contraseña debe tener al menos 6 caracteres, incluir al menos una mayúscula, un número y un carácter especial.");
        }

        if (registroDTO.getFechaNacimiento() == null || registroDTO.getFechaNacimiento().isAfter(LocalDate.now()) ||
                registroDTO.getFechaNacimiento().isAfter(LocalDate.now().minusYears(18))) {
            return ResponseEntity.badRequest().body("El usuario debe tener al menos 18 años.");
        }

        if (registroDTO.getRol() == null ||
                !EnumSet.allOf(Rol.class).contains(registroDTO.getRol())) {
            return ResponseEntity.badRequest().body("El rol no es válido o está vacío.");
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



    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            String token = authenticationService.autenticarUsuario(
                    loginRequestDTO.getEmail(),
                    loginRequestDTO.getContrasena()
            );
            return ResponseEntity.ok( token);
        } catch (Exception e) {
            log.error("Error interno en /login", e);
            // Para depurar, devuelvo el mensaje real; luego lo puedes enmascarar otra vez
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }


    /**
     * Endpoint para obtener el perfil del usuario autenticado
     * @return Datos del perfil del usuario
     */
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfilUsuario() {
        try {
            Map<String, Object> datosUsuario = perfilUsuarioServicio.obtenerDatosPerfil();
            return ResponseEntity.ok(datosUsuario);
        } catch (RuntimeException e) {
            if (e.getMessage().equals("No autenticado")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No autenticado");
            } else if (e.getMessage().equals("Usuario no encontrado")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno");
            }
        }
    }



}
