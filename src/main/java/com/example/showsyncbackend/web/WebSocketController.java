package com.example.showsyncbackend.web;

import com.example.showsyncbackend.dtos.MensajeDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

public class WebSocketController {

    @MessageMapping("/enviar")
    @SendTo("/tema/actualizacion")
    public MensajeDTO enviarMensaje(MensajeDTO mensaje) {
        return mensaje;
    }
}
