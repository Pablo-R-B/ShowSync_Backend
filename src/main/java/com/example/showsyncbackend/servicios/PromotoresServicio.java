package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class PromotoresServicio {

    private final PromotoresRepositorio promotoresRepository;
    private final EventosRepositorio eventosRepository;

    //  Obtener perfil del promotor
    public Promotores obtenerPromotorPorId(Integer id) {
        return promotoresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
    }

    //  Listar todos los promotores
    public List<Promotores> listarPromotores() {
        return promotoresRepository.findAll();
    }

    //  Listar eventos de un promotor (opcionalmente puedes luego filtrar por pasados / futuros)
    public List<Eventos> listarEventosDePromotor(Integer id, String tipo) {
        return eventosRepository.findByPromotorId(id);
    }

    //  Solicitar sala (esto luego se podría ampliar para enviar notificaciones o guardar en BD)
    public void solicitarSala(Integer id, String sala) {
        Promotores promotor = obtenerPromotorPorId(id);
        System.out.println("El promotor " + promotor.getNombrePromotor() + " ha solicitado la sala: " + sala);
    }

    //  Crear evento para un promotor
    public Eventos crearEvento(Integer id, Eventos evento) {
        Promotores promotor = obtenerPromotorPorId(id);
        evento.setPromotor(promotor);
        return eventosRepository.save(evento);
    }

    //  Editar evento existente
    public Eventos editarEvento(Integer promotorId, Integer eventoId, Eventos eventoActualizado) {
        Eventos eventoExistente = eventosRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (!eventoExistente.getPromotor().getId().equals(promotorId)) {
            throw new RuntimeException("El evento no pertenece al promotor especificado");
        }

        eventoExistente.setNombre_evento(eventoActualizado.getNombre_evento());
        eventoExistente.setDescripcion(eventoActualizado.getDescripcion());
        eventoExistente.setFecha_evento(eventoActualizado.getFecha_evento());
        eventoExistente.setSala_id(eventoActualizado.getSala_id());
        eventoExistente.setEstado(eventoActualizado.getEstado());
        eventoExistente.setImagen_evento(eventoActualizado.getImagen_evento());
        eventoExistente.setGenerosMusicales(eventoActualizado.getGenerosMusicales());

        return eventosRepository.save(eventoExistente);
    }


    //  Eliminar evento existente
    public void eliminarEvento(Integer promotorId, Integer eventoId) {
        Eventos evento = eventosRepository.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (!evento.getPromotor().getId().equals(promotorId)) {
            throw new RuntimeException("El evento no pertenece al promotor especificado");
        }

        eventosRepository.delete(evento);
    }

}
