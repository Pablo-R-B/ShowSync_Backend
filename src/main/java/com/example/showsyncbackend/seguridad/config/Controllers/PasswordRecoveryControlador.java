package com.example.showsyncbackend.seguridad.config.Controllers;

import com.example.showsyncbackend.modelos.TokenRecuperacion;
import com.example.showsyncbackend.seguridad.config.services.PasswordRecoveryService;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/password-recovery")
public class PasswordRecoveryControlador {

    private final PasswordRecoveryService passwordRecoveryService;
    private final UsuarioRepositorio usuarioRepositorio;

    public PasswordRecoveryControlador(PasswordRecoveryService passwordRecoveryService, UsuarioRepositorio usuarioRepositorio) {
        this.passwordRecoveryService = passwordRecoveryService;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    // Endpoint para solicitar un token de recuperación de contraseña
    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordRecovery(@RequestParam String email) {
        // Verificar si el usuario existe
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar el token y enviarlo por correo
        String token = passwordRecoveryService.generarTokenRecuperacion(email);

        return ResponseEntity.ok("Correo de recuperación enviado. Si no ves el correo, revisa tu carpeta de spam.");
    }

    // Endpoint para restablecer la contraseña
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String nuevaContrasena) {
        try {
            // Validar el token de recuperación
            TokenRecuperacion tokenRecuperacion = passwordRecoveryService.validarToken(token);

            // Restablecer la contraseña del usuario asociado al token
            passwordRecoveryService.restablecerContrasena(tokenRecuperacion.getUsuario(), nuevaContrasena);

            return ResponseEntity.ok("Contraseña restablecida correctamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
