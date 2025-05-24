package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.RespuestaEventoRevisionDTO;
import com.example.showsyncbackend.dtos.EventosDTO;
import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.*;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.GenerosMusicalesRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import com.example.showsyncbackend.repositorios.SalasRepositorio;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
public class EventosServicio {

    @Autowired
    private EventosRepositorio eventosRepositorio;


    @Autowired
    private PromotoresRepositorio promotoresRepositorio;


    @Autowired
    private GenerosMusicalesRepositorio generosMusicalesRepositorio;

    @Autowired
    private SalasRepositorio salasRepositorio;



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
                        .fechaEvento(evento.getFechaEvento())
                        .estado(evento.getEstado())
                        .imagenEvento(evento.getImagen_evento())
                        .idSala(evento.getSala().getId())
                        .nombreSala(evento.getSala().getNombre())
                        .idPromotor(evento.getPromotor().getId())
                        .nombrePromotor(evento.getPromotor().getNombrePromotor())
                        .build())
                .collect(Collectors.toList());
    }

    //Obtener id y nombre de todos los eventos de un promotor mediante id de usuario
    public List<EventosDTO> listarEventosPorIdUsuario(Integer idUsuario) {
        Promotores promotor = promotoresRepositorio.findByUsuarioId(idUsuario)
                .orElseThrow(()-> new EntityNotFoundException("Promotor no encontrado"));
        Integer promotorId = promotor.getId();
        List<Eventos> eventos = eventosRepositorio.findByPromotorId(promotorId);


        return eventos.stream().map( ev -> EventosDTO.builder()
                .id(ev.getId())
                .nombreEvento(ev.getNombre_evento())
                .build()).toList();
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
        eventoExistente.setFechaEvento(eventoActualizado.getFechaEvento());
        eventoExistente.setSala(eventoActualizado.getSala());
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
        dto.setFechaEvento(eventoGuardado.getFechaEvento());
        dto.setIdSala(eventoGuardado.getSala() != null ? eventoGuardado.getSala().getId() : null);
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


        eventosRepositorio.delete(evento);
    }

    // Listar todos los eventos
    public List<EventosDTO> listarTodosLosEventos() {
        List<Eventos> eventos = eventosRepositorio.findAll();

        return eventos.stream().map(evento -> new EventosDTO(
                evento.getId(),
                evento.getNombre_evento(),
                evento.getDescripcion(),
                evento.getFechaEvento(),
                evento.getEstado(),
                evento.getImagen_evento(),
                evento.getSala() != null ? evento.getSala().getId() : null,
                evento.getSala() != null ? evento.getSala().getNombre() : null,
                evento.getPromotor() != null ? evento.getPromotor().getId() : null,
                evento.getPromotor() != null ? evento.getPromotor().getNombrePromotor() : null,
                evento.getGenerosMusicales() != null ? evento.getGenerosMusicales().stream()
                        .map(g -> g.getNombre())
                        .collect(Collectors.toSet()) : null,
                evento.getArtistasAsignados() != null ? evento.getArtistasAsignados().stream()
                        .map(a -> a.getNombreArtista())
                        .collect(Collectors.toSet()) : null
        )).collect(Collectors.toList());
    }



    // Obtener evento por ID

    public EventosDTO obtenerEventoPorId(Integer eventoId) {
        Eventos evento = eventosRepositorio.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        Hibernate.initialize(evento.getGenerosMusicales());  // Inicializar la colección
        return new EventosDTO(
                evento.getId(),
                evento.getNombre_evento(),
                evento.getDescripcion(),
                evento.getFechaEvento(),
                evento.getEstado(),
                evento.getImagen_evento(),
                evento.getSala().getId(),
                evento.getSala().getNombre(),
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
        List<Estado> estadosDeseados = List.of(Estado.en_revision, Estado.confirmado);
        List<Eventos> eventos = eventosRepositorio.findByEstadoIn(estadosDeseados);

        return eventos.stream()
                .map(evento -> EventosDTO.builder()
                        .id(evento.getId())
                        .nombreEvento(evento.getNombre_evento())
                        .descripcion(evento.getDescripcion())
                        .fechaEvento(evento.getFechaEvento())
                        .estado(evento.getEstado())
                        .imagenEvento(evento.getImagen_evento())
                        .idSala(evento.getSala().getId())
                        .nombreSala(evento.getSala().getNombre())
                        .idPromotor(evento.getPromotor().getId())
                        .nombrePromotor(evento.getPromotor().getNombrePromotor())
                        .build())
                .collect(Collectors.toList());
    }



    // Actualizar el seguimiento de un evento

    public void actualizarSeguimiento(Integer id, boolean seguido) {
        // Buscar el evento por ID
        Eventos evento = eventosRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + id));

        // Actualizar el estado del campo seguido
        evento.setSeguido(seguido);

        // Guardar el evento actualizado
        eventosRepositorio.save(evento);
    }

    public List<String> obtenerGeneros() {
        try {
            return eventosRepositorio.findDistinctGeneros();
        } catch (Exception e) {
            // Manejo de error
            throw new RuntimeException("Error al obtener géneros musicales", e);
        }
    }

    public List<Estado> obtenerEstados() {
        try {
            return eventosRepositorio.findDistinctEstados();
        } catch (Exception e) {
            // Manejo de error
            throw new RuntimeException("Error al obtener estados", e);
        }
    }


    // Cambiar estado a confirmado
    public void confirmarEvento(Integer eventoId) {
        cambiarEstadoEvento(eventoId, Estado.confirmado);
    }

    // Cambiar estado a cancelado
    public void cancelarEvento(Integer eventoId) {
        cambiarEstadoEvento(eventoId, Estado.cancelado);
    }

    // Método genérico para cambiar el estado del evento
    public void cambiarEstadoEvento(Integer eventoId, Estado nuevoEstado) {
        Eventos evento = eventosRepositorio.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado con id: " + eventoId));

        evento.setEstado(nuevoEstado);

        eventosRepositorio.save(evento);
    }

    public RespuestaEventoRevisionDTO crearEventoEnRevision(EventosDTO dto) {
        // Obtener id usuario desde JWT
        Integer idUsuario = obtenerIdUsuarioDesdeJWT();

        // Buscar promotor por ID de usuario
        Promotores promotor = promotoresRepositorio.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Promotor no encontrado"));

        // Buscar sala
        Salas sala = salasRepositorio.findById(dto.getIdSala())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sala no encontrada"));

        // Validar que la fecha del evento no sea anterior a la fecha actual
        LocalDate fechaEvento = dto.getFechaEvento();
        LocalDate fechaActual = LocalDate.now();

        if (fechaEvento.isBefore(fechaActual)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No se puede crear un evento en una fecha pasada. La fecha debe ser igual o posterior a la fecha actual.");
        }

        // Validar disponibilidad de sala en la fecha
        if (eventosRepositorio.existsBySalaAndFecha(sala.getId(), fechaEvento)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La sala ya está reservada para esa fecha. Backen");
        }

        // Buscar géneros musicales por nombre
        Set<GenerosMusicales> generos = new HashSet<>();
        if (dto.getGenerosMusicales() != null && !dto.getGenerosMusicales().isEmpty()) {
            generos = new HashSet<>(generosMusicalesRepositorio.findByNombreIn(dto.getGenerosMusicales()));
            if (generos.size() != dto.getGenerosMusicales().size()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Uno o más géneros musicales no son válidos.");
            }
        }

        // Crear evento
        Eventos evento = Eventos.builder()
                .nombre_evento(dto.getNombreEvento())
                .descripcion(dto.getDescripcion())
                .fechaEvento(dto.getFechaEvento())
                .imagen_evento(dto.getImagenEvento())
                .sala(sala)
                .promotor(promotor)
                .estado(Estado.en_revision)
                .generosMusicales(generos)
                .build();

        // Guardar evento
        evento = eventosRepositorio.save(evento);

        // Convertir géneros a Set<String> para la respuesta
        Set<String> nombresGeneros = generos.stream()
                .map(GenerosMusicales::getNombre)
                .collect(Collectors.toSet());

        // Construir respuesta
        return RespuestaEventoRevisionDTO.builder()
                .id(evento.getId())
                .nombreEvento(evento.getNombre_evento())
                .descripcion(evento.getDescripcion())
                .fechaEvento(evento.getFechaEvento())
                .estado(evento.getEstado())
                .imagenEvento(evento.getImagen_evento())
                .idSala(sala.getId())
                .nombreSala(sala.getNombre())
                .nombrePromotor(promotor.getNombrePromotor())
                .generosMusicales(nombresGeneros)
                .build();
    }

    public Integer obtenerIdUsuarioDesdeJWT() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No autenticado");
        }

        Claims claims = (Claims) auth.getPrincipal();
        return ((Number) claims.get("id")).intValue();
    }






    // Obtener evento por promotor y eventoId
    public EventosDTO obtenerEventoPorPromotor(Integer promotorId, Integer eventoId) {
        Eventos evento = eventosRepositorio.findById(eventoId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (!evento.getPromotor().getId().equals(promotorId)) {
            throw new RuntimeException("El evento no pertenece al promotor especificado");
        }

        EventosDTO dto = new EventosDTO();
        dto.setId(evento.getId());
        dto.setNombreEvento(evento.getNombre_evento());
        dto.setDescripcion(evento.getDescripcion());
        dto.setFechaEvento(evento.getFecha_evento());
        dto.setIdSala(evento.getSala_id() != null ? evento.getSala_id().getId() : null);
        dto.setEstado(evento.getEstado());
        dto.setImagenEvento(evento.getImagen_evento());

        return dto;
    }



}