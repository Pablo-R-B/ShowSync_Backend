package com.example.showsyncbackend.servicios;


import com.example.showsyncbackend.dtos.EventosDTO;
import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class EventosServicio {

    @Autowired
    private EventosRepositorio eventosRepositorio;


    @Autowired
    private PromotoresRepositorio promotoresRepositorio;


    // Constructor con dependencia para inyección de eventosRepositorio
    public EventosServicio(EventosRepositorio eventosRepositorio) {
        this.eventosRepositorio = eventosRepositorio;
    }

    // Obtener todos los eventos confirmados
    public List<EventosDTO> obtenerEventosConfirmados() {
        // Busca todos los eventos que tienen el estado confirmado
        return getEventosDTOS();
    }

    // Obtener todos los eventos de un promotor
    public List<EventosDTO> obtenerEventosDePromotor(Integer promotorId) {
        // Busca los eventos del promotor especificado
        List<Eventos> eventos = eventosRepositorio.findByPromotorId(promotorId);

        return eventos.stream()
                .map(evento -> EventosDTO.builder()
                        .id(evento.getId())
                        .nombreEvento(evento.getNombre_evento())
                        .descripcion(evento.getDescripcion())
                        .fechaEvento(evento.getFecha_evento())
                        .estado(evento.getEstado())
                        .imagenEvento(evento.getImagen_evento())
                        .idSala(evento.getSala_id().getId())
                        .nombreSala(evento.getSala_id().getNombre())
                        .idPromotor(evento.getPromotor().getId())
                        .nombrePromotor(evento.getPromotor().getNombrePromotor())
                        .build())
                .collect(Collectors.toList());
    }

    // Crear un nuevo evento para un promotor
    public Eventos crearEvento(Integer promotorId, Eventos eventoNuevo) {
        // Verificar que el promotor exista
        Promotores promotor = promotoresRepositorio.findById(promotorId)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));

        // Asignar al evento el promotor que lo crea
        eventoNuevo.setPromotor(promotor);

        // Guardar el nuevo evento
        return eventosRepositorio.save(eventoNuevo);
    }

    // Editar evento existente
    public EventosDTO editarEvento(Integer promotorId, Eventos eventoActualizado) {
        // Buscar evento existente por ID
        Eventos eventoExistente = eventosRepositorio.findById(eventoActualizado.getId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Verificar que el promotor tenga acceso al evento
        if (!eventoExistente.getPromotor().getId().equals(promotorId)) {
            throw new RuntimeException("El evento no pertenece al promotor especificado");
        }

        // Actualizar los datos del evento
        eventoExistente.setNombre_evento(eventoActualizado.getNombre_evento());
        eventoExistente.setDescripcion(eventoActualizado.getDescripcion());
        eventoExistente.setFecha_evento(eventoActualizado.getFecha_evento());
        eventoExistente.setSala_id(eventoActualizado.getSala_id());
        eventoExistente.setEstado(eventoActualizado.getEstado());
        eventoExistente.setImagen_evento(eventoActualizado.getImagen_evento());
        eventoExistente.setGenerosMusicales(eventoActualizado.getGenerosMusicales());

        // Guardar los cambios
        Eventos eventoGuardado = eventosRepositorio.save(eventoExistente);

        // Devolver solo los datos necesarios con un DTO construido manualmente
        EventosDTO dto = new EventosDTO();
        dto.setId(eventoGuardado.getId());
        dto.setNombreEvento(eventoGuardado.getNombre_evento());
        dto.setDescripcion(eventoGuardado.getDescripcion());
        dto.setFechaEvento(eventoGuardado.getFecha_evento());
        dto.setIdSala(eventoGuardado.getSala_id() != null ? eventoGuardado.getSala_id().getId() : null);
        dto.setEstado(eventoGuardado.getEstado());
        dto.setImagenEvento(eventoGuardado.getImagen_evento());


        return dto;

    }


    // Eliminar evento existente
    public void eliminarEvento(Integer promotorId, Integer eventoId) {
        // Buscar evento por ID
        Eventos evento = eventosRepositorio.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Verificar que el promotor tenga acceso al evento
        if (!evento.getPromotor().getId().equals(promotorId)) {
            throw new RuntimeException("El evento no pertenece al promotor especificado");
        }

        // Eliminar evento
        eventosRepositorio.delete(evento);
    }

    // Listar todos los eventos
    public List<Eventos> listarTodosLosEventos() {
        return eventosRepositorio.findAll();
    }

    // Mostrar perfil con datos del evento

    // Mostrar perfil con datos del evento

    public EventosDTO obtenerEventoPorId(Integer eventoId) {
        Eventos evento = eventosRepositorio.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        Hibernate.initialize(evento.getGenerosMusicales());  // Inicializar la colección
        return new EventosDTO(
                evento.getId(),
                evento.getNombre_evento(),
                evento.getDescripcion(),
                evento.getFecha_evento(),
                evento.getEstado(),
                evento.getImagen_evento(),
                evento.getSala_id().getId(),
                evento.getSala_id().getNombre(),
                evento.getPromotor().getId(),
                evento.getPromotor().getNombrePromotor(),
                evento.getGenerosMusicales().stream().map(GenerosMusicales::getNombre).collect(Collectors.toSet()),
                evento.getArtistasAsignados().stream().map(Artistas::getNombreArtista).collect(Collectors.toSet())
        );
    }

    // Obtener catalogo de eventos
    public List<EventosDTO> obtenerCatalogoEventos() {
        return getEventosDTOS();
    }

    private List<EventosDTO> getEventosDTOS() {
        List<Eventos> eventos = eventosRepositorio.findByEstado(Estado.confirmado);

        return eventos.stream()
                .map(evento -> EventosDTO.builder()
                        .id(evento.getId())
                        .nombreEvento(evento.getNombre_evento())
                        .descripcion(evento.getDescripcion())
                        .fechaEvento(evento.getFecha_evento())
                        .estado(evento.getEstado())
                        .imagenEvento(evento.getImagen_evento())
                        .idSala(evento.getSala_id().getId())
                        .nombreSala(evento.getSala_id().getNombre())
                        .idPromotor(evento.getPromotor().getId())
                        .nombrePromotor(evento.getPromotor().getNombrePromotor())
                        .build())
                .collect(Collectors.toList());
    }


}