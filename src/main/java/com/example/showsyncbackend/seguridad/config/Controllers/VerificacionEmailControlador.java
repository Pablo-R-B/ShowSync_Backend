package com.example.showsyncbackend.seguridad.config.Controllers;

import com.example.showsyncbackend.seguridad.config.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerificacionEmailControlador {

    private final AuthenticationService authenticationService; // Inyectamos el servicio

    // Endpoint para verificar el email
    @GetMapping("/verificar-email")
    public ResponseEntity<String> verificarEmail(@RequestParam("token") String token) {
        // Llamar al servicio para verificar el email usando el token
        boolean verificado = authenticationService.verificarEmail(token);

        if (verificado) {
            return ResponseEntity.ok("Tu cuenta ha sido verificada. Ahora puedes iniciar sesión.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token de verificación inválido.");
        }
    }
}
