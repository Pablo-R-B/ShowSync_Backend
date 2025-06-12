package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Conversacion;
import com.example.showsyncbackend.modelos.MensajeChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MensajeChatRepositorio extends JpaRepository<MensajeChat, Long> {
    /**
     * Busca todos los mensajes para una conversación específica, ordenados por la fecha de envío de forma ascendente.
     * @param conversacion La entidad Conversacion por la que buscar mensajes.
     * @return Una lista de MensajeChat.
     */
    List<MensajeChat> findByConversacionOrderByFechaEnvioAsc(Conversacion conversacion);
}
