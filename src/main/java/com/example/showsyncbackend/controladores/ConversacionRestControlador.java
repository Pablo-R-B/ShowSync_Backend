package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.MensajeChatDTO;
import com.example.showsyncbackend.modelos.Conversacion;
import com.example.showsyncbackend.modelos.MensajeChat;
import com.example.showsyncbackend.seguridad.config.services.ConversacionService;
import com.example.showsyncbackend.dtos.MensajeDTO;
import com.example.showsyncbackend.servicios.MensajeServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/conversaciones")
public class ConversacionRestControlador {

    private final ConversacionService conversacionService;
    private final MensajeServicio mensajeServicio;

    @Autowired
    public ConversacionRestControlador(ConversacionService conversacionService, MensajeServicio mensajeServicio) {
        this.conversacionService = conversacionService;
        this.mensajeServicio = mensajeServicio;
    }

    /**
     * Endpoint para obtener o crear una conversación entre un artista y un promotor.
     * URL: GET /conversaciones/{artistaId}/{promotorId}
     *
     * @param artistaId El ID del artista.
     * @param promotorId El ID del promotor.
     * @return ResponseEntity con el ID de la conversación (chatId) y HttpStatus.OK.
     */
    @GetMapping("/{artistaId}/{promotorId}")
    public ResponseEntity<Long> getOrCreateConversation(
            @PathVariable Long artistaId,
            @PathVariable Long promotorId) {
        // Ahora se está llamando al método getOrCreateConversation del servicio correcto
        Long chatId = conversacionService.getOrCreateConversation(artistaId, promotorId);
        return new ResponseEntity<>(chatId, HttpStatus.OK);
    }

    /**
     * Endpoint para obtener el historial de mensajes de una conversación específica.
     *
     * URL: GET /conversaciones/{chatId}/mensajes
     *
     * @param chatId El ID de la conversación.
     * @return ResponseEntity con una lista de MensajeChatDTO (para el frontend) y HttpStatus.OK.
     */
    @GetMapping("/{chatId}/mensajes")
    public ResponseEntity<List<MensajeChatDTO>> getConversationMessages(@PathVariable Long chatId) {
        List<MensajeChatDTO> mensajesDTO = mensajeServicio.getHistorialMensajes(chatId);
        return new ResponseEntity<>(mensajesDTO, HttpStatus.OK);
    }
}
