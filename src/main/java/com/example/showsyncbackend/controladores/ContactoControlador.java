package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ContactoDTO;
import com.example.showsyncbackend.seguridad.config.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/contacto")
public class ContactoControlador {
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<?> enviarMensajeContacto(@RequestBody ContactoDTO dto) {
        String asunto = "📩 Nuevo mensaje de contacto - ShowSync";
        String contenidoHtml = "<html><body style='font-family:sans-serif;'>" +
                "<h2>Nuevo mensaje recibido</h2>" +
                "<p><strong>Nombre:</strong> " + dto.getNombre() + "</p>" +
                "<p><strong>Email:</strong> " + dto.getEmail() + "</p>" +
                "<p><strong>Mensaje:</strong></p>" +
                "<p>" + dto.getMensaje().replace("\n", "<br>") + "</p>" +
                "</body></html>";

        emailService.enviarCorreo("showsync.empresa@gmail.com", asunto, contenidoHtml);
        return ResponseEntity.ok("Mensaje enviado correctamente");
    }
}
