package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ContactoDTO;
import com.example.showsyncbackend.dtos.RespuestaContactoDTO;
import com.example.showsyncbackend.seguridad.config.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/contacto")
public class ContactoControlador {
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity<RespuestaContactoDTO> enviarMensajeContacto(@RequestBody ContactoDTO dto) {
        try {
            if (!dto.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                return ResponseEntity.badRequest()
                        .body(new RespuestaContactoDTO(false, "El email proporcionado no es válido."));
            }

            String asunto = "📩 Nuevo mensaje de contacto - ShowSync";
            String contenidoHtml = "<html><body style='font-family:sans-serif;'>" +
                    "<h2>Nuevo mensaje recibido</h2>" +
                    "<p><strong>Nombre:</strong> " + dto.getNombre() + "</p>" +
                    "<p><strong>Email:</strong> " + dto.getEmail() + "</p>" +
                    "<p><strong>Mensaje:</strong></p>" +
                    "<p>" + dto.getMensaje().replace("\n", "<br>") + "</p>" +
                    "</body></html>";

            emailService.enviarCorreo("showsync.empresa@gmail.com", asunto, contenidoHtml);

            return ResponseEntity.ok(new RespuestaContactoDTO(true, "¡Tu mensaje ha sido enviado correctamente!"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new RespuestaContactoDTO(false, "Error al enviar el mensaje: " + e.getMessage()));
        }
    }

}
