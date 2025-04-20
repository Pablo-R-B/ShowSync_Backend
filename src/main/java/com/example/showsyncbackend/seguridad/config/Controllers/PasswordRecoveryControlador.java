package com.example.showsyncbackend.seguridad.config.Controllers;

import com.example.showsyncbackend.modelos.TokenRecuperacion;
import com.example.showsyncbackend.seguridad.config.services.PasswordRecoveryService;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/auth/password-recovery")
@RequiredArgsConstructor
public class PasswordRecoveryControlador {

    private final PasswordRecoveryService passwordRecoveryService;
    private final UsuarioRepositorio usuarioRepositorio;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordRecovery(@RequestParam String email) {
        try {
            passwordRecoveryService.generarTokenRecuperacion(email);
            return ResponseEntity.ok("Correo de recuperación enviado. Revisa tu bandeja de entrada o spam.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String nuevaContrasena) {

        try {
            passwordRecoveryService.restablecerContrasena(
                    passwordRecoveryService.validarToken(token).getUsuario(),
                    nuevaContrasena
            );
            return ResponseEntity.ok("Contraseña actualizada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}