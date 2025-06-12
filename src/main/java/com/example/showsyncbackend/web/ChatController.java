package com.example.showsyncbackend.web;

import com.example.showsyncbackend.dtos.MensajeChatDTO;
// No necesitamos MensajeDTO aquí si solo usas MensajeChatDTO para el chat
// import com.example.showsyncbackend.dtos.MensajeDTO;
import com.example.showsyncbackend.servicios.MensajeServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.Instant; // ¡CRÍTICO! Cambiado a Instant
// import java.time.LocalDateTime; // Ya no se necesita aquí

@Controller
public class ChatController {

    @Autowired
    private MensajeServicio mensajeServicio;

    @MessageMapping("/chat/{chatId}/enviar")
    @SendTo("/tema/chat/{chatId}/mensajes") // Retransmite a todos los suscritos a este tema
    public MensajeChatDTO responder(@DestinationVariable Long chatId, MensajeChatDTO mensaje) {
        System.out.println("DEBUG: Mensaje recibido en ChatController para chat " + chatId + " de " + mensaje.getRemitente() + " con contenido: " + mensaje.getContenido());

        // Asegúrate de que la fecha de envío esté establecida. Si el frontend ya la envía, esto solo la verifica.
        // Si por alguna razón es null (no debería pasar con toISOString()), asigna Instant.now().
        if (mensaje.getFechaEnvio() == null) {
            mensaje.setFechaEnvio(Instant.now()); // ¡CRÍTICO! Usar Instant.now()
            System.out.println("DEBUG: Fecha de envío establecida en ChatController: " + mensaje.getFechaEnvio());
        }

        // Llama al servicio para guardar el mensaje en la base de datos
        MensajeChatDTO savedMensaje = mensajeServicio.guardarMensaje(chatId, mensaje);
        System.out.println("DEBUG: Mensaje procesado y guardado por servicio. Retornando para retransmisión.");

        return savedMensaje; // Devuelve el mensaje guardado (que se retransmitirá)
    }
}
