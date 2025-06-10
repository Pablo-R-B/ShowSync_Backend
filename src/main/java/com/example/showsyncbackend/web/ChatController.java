package com.example.showsyncbackend.web;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Recibe mensaje en /app/enviar (igual que en Angular)
    @MessageMapping("/enviar")
    @SendTo("/tema/actualizacion")
    public String responder(String mensaje) {
        System.out.println("Mensaje recibido en backend: " + mensaje);
        // Aquí simplemente devolvemos un mensaje de respuesta simulado
        return "Respuesta del backend: " + mensaje.toUpperCase();
    }
}