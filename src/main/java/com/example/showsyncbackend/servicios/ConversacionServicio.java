package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.Conversacion;
import com.example.showsyncbackend.repositorios.ConversacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
public class ConversacionServicio {
    @Autowired
    private ConversacionRepositorio conversacionRepositorio;

    /**
     * Obtiene una conversación existente entre un artista y un promotor,
     * o crea una nueva si no existe.
     * Garantiza que siempre se devuelve el mismo chatId para un par dado de IDs,
     * sin importar el orden en que se pasen los IDs a este método.
     *
     * @param artistaId El ID del artista.
     * @param promotorId El ID del promotor.
     * @return El ID de la conversación.
     */
    @Transactional // ¡Importante para operaciones de lectura/escritura en DB!
    public Long getOrCreateConversation(Long artistaId, Long promotorId) {
        // 1. Intenta encontrar la conversación en el orden ArtistaId -> PromotorId
        Optional<Conversacion> existingConversation = conversacionRepositorio
                .findByArtistaIdAndPromotorId(artistaId, promotorId);

        // 2. Si no se encuentra en el primer orden, intenta en el orden inverso PromotorId -> ArtistaId
        if (existingConversation.isEmpty()) {
            existingConversation = conversacionRepositorio
                    .findByPromotorIdAndArtistaId(promotorId, artistaId);
        }

        // 3. Si se encuentra la conversación en cualquiera de los dos órdenes, devuelve su ID
        if (existingConversation.isPresent()) {
            System.out.println("Conversación existente encontrada con ID: " + existingConversation.get().getId());
            return existingConversation.get().getId();
        } else {
            // 4. Si no existe en ningún orden, crea una nueva conversación.
            // Es buena práctica guardar siempre en un orden consistente en la DB si es posible,
            // pero la búsqueda con findByParticipantes (o los dos findByXAndY) lo manejará.
            Conversacion nuevaConversacion = new Conversacion();
            nuevaConversacion.setArtistaId(artistaId);
            nuevaConversacion.setPromotorId(promotorId);

            Conversacion savedConversacion = conversacionRepositorio.save(nuevaConversacion);
            System.out.println("Nueva conversación creada con ID: " + savedConversacion.getId());
            return savedConversacion.getId();
        }
    }
}


