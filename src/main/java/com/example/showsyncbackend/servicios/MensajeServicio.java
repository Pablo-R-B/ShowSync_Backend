package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.MensajeChatDTO;
import com.example.showsyncbackend.modelos.Conversacion;
import com.example.showsyncbackend.modelos.MensajeChat;
import com.example.showsyncbackend.repositorios.ConversacionRepositorio;
import com.example.showsyncbackend.repositorios.MensajeChatRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class MensajeServicio {

    @Autowired
    private MensajeChatRepositorio mensajeChatRepositorio;

    @Autowired
    private ConversacionRepositorio conversacionRepositorio;

    /**
     * Guarda un nuevo mensaje en la base de datos.
     * @param chatId El ID de la conversación a la que pertenece el mensaje.
     * @param mensajeDTO El DTO del mensaje recibido del cliente.
     * @return El DTO del mensaje guardado.
     */
    @Transactional
    public MensajeChatDTO guardarMensaje(Long chatId, MensajeChatDTO mensajeDTO) {
        System.out.println("DEBUG: Iniciando guardarMensaje para chatId: " + chatId + ", contenido: " + mensajeDTO.getContenido());
        try {
            Conversacion conversacion = conversacionRepositorio.findById(chatId)
                    .orElseThrow(() -> new RuntimeException("Conversación no encontrada con ID: " + chatId));
            System.out.println("DEBUG: Conversación encontrada: " + conversacion.getId());

            MensajeChat mensaje = new MensajeChat();
            mensaje.setConversacion(conversacion);
            mensaje.setContenido(mensajeDTO.getContenido());

            mensaje.setRemitente(mensajeDTO.getRemitente());
            mensaje.setTipo(mensajeDTO.getTipo().toUpperCase()); // Convertir a mayúsculas para la DB
            mensaje.setImagenRemitenteUrl(mensajeDTO.getImagenRemitenteUrl());
            // ¡CRÍTICO! Asignar Instant
            mensaje.setFechaEnvio(mensajeDTO.getFechaEnvio());

            System.out.println("DEBUG: Entidad MensajeChat creada. Contenido: " + mensaje.getContenido() + ", Remitente: " + mensaje.getRemitente() + ", Tipo (guardado): " + mensaje.getTipo() + ", Fecha: " + mensaje.getFechaEnvio());

            MensajeChat savedMensaje = mensajeChatRepositorio.save(mensaje);
            System.out.println("✅ Mensaje guardado en BD con ID: " + savedMensaje.getId() + " para chat: " + chatId + " (¡Persistencia OK!)");

            // ¡CRÍTICO! Copiar el temporalId del DTO de entrada al DTO de salida, y también el ID real
            return MensajeChatDTO.builder()
                    .id(savedMensaje.getId()) // ID real del backend
                    .contenido(savedMensaje.getContenido())
                    .remitente(savedMensaje.getRemitente())
                    .tipo(savedMensaje.getTipo().toLowerCase()) // Devolver en minúsculas para el frontend
                    .imagenRemitenteUrl(savedMensaje.getImagenRemitenteUrl())
                    .fechaEnvio(savedMensaje.getFechaEnvio())
                    .temporalId(mensajeDTO.getTemporalId()) // <-- ¡CRÍTICO! PASAR EL temporalId DE VUELTA
                    .build();
        } catch (Exception e) {
            System.err.println("❌ ERROR FATAL al guardar mensaje en MensajeServicio:");
            System.err.println("   Mensaje de error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Obtiene el historial de mensajes para una conversación.
     * @param chatId El ID de la conversación.
     * @return Una lista de MensajeChatDTOs.
     */
    @Transactional(readOnly = true)
    public List<MensajeChatDTO> getHistorialMensajes(Long chatId) {
        Conversacion conversacion = conversacionRepositorio.findById(chatId)
                .orElseThrow(() -> new RuntimeException("Conversación no encontrada con ID: " + chatId));

        List<MensajeChat> mensajes = mensajeChatRepositorio.findByConversacionOrderByFechaEnvioAsc(conversacion);

        return mensajes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para mapear de MensajeChat (entidad) a MensajeChatDTO (DTO)
    private MensajeChatDTO mapToDTO(MensajeChat mensaje) {
        return MensajeChatDTO.builder()
                .id(mensaje.getId()) // Mapea el ID real para el historial
                .contenido(mensaje.getContenido())
                .remitente(mensaje.getRemitente())
                .tipo(mensaje.getTipo().toLowerCase())
                .imagenRemitenteUrl(mensaje.getImagenRemitenteUrl())
                .fechaEnvio(mensaje.getFechaEnvio())
                // El historial no tiene temporalId, así que no se mapea aquí.
                .build();
    }
}
