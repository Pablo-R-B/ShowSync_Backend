package com.example.showsyncbackend.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class NotificacionController {

    @MessageMapping("/mensaje") // el cliente enviará a /app/mensaje
    @SendTo("/tema/notificaciones") // el servidor responderá a los subscritos a /tema/notificaciones
    public MensajeDTO enviarMensaje(MensajeDTO mensaje) {
        System.out.println("Mensaje recibido de: " + mensaje.getRemitente() +
                " con contenido: " + mensaje.getContenido());
        return mensaje; // responder con el mismo objeto
    }
}
