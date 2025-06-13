package com.example.showsyncbackend.seguridad.config.services;


import com.example.showsyncbackend.modelos.Conversacion;

import com.example.showsyncbackend.repositorios.ConversacionRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ConversacionService {

    @Autowired
    private final ConversacionRepositorio conversacionRepositorio;

    @Autowired
    public ConversacionService(ConversacionRepositorio conversacionRepositorio) {
        this.conversacionRepositorio = conversacionRepositorio;
    }

    @Transactional // ¡Importante para operaciones de lectura/escritura en DB!
    public Long getOrCreateConversation(Long artistaId, Long promotorId) {
        // Usa la consulta robusta para encontrar la conversación sin importar el orden
        // Asumo que tienes el método findByParticipantes en tu ConversacionRepositorio
        Optional<Conversacion> existingConversation = conversacionRepositorio
                .findByParticipantes(artistaId, promotorId);

        if (existingConversation.isPresent()) {
            System.out.println("Conversación existente encontrada con ID: " + existingConversation.get().getId());
            return existingConversation.get().getId();
        } else {
            // Si no existe, crea una nueva conversación
            Conversacion nuevaConversacion = new Conversacion();
            nuevaConversacion.setArtistaId(artistaId);
            nuevaConversacion.setPromotorId(promotorId);

            Conversacion savedConversacion = conversacionRepositorio.save(nuevaConversacion);
            System.out.println("Nueva conversación creada con ID: " + savedConversacion.getId());
            return savedConversacion.getId();
        }
    }
}
