package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.PostulacionDTO;
import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import com.example.showsyncbackend.enumerados.TipoSolicitud;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.PostulacionEvento;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PostulacionEventosRepositorio;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostulacionEventosServicio {
    private final EventosRepositorio eventosRepositorio;
    private PostulacionEventosRepositorio postulacionEventosRepositorio;
    private EventosRepositorio eventosRepository;
    private ArtistasRepositorio artistasRepositorio;

    @Transactional
    public void nuevaSolicitud(Integer eventoId, Integer artistaId, TipoSolicitud tipo){
        Eventos evento = eventosRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));
        Artistas artista = artistasRepositorio.findById(artistaId)
                .orElseThrow(() -> new EntityNotFoundException("Artista no encontrado"));

        if (evento.getFechaEvento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede crear una solicitud para un evento pasado.");
        }

        Optional<PostulacionEvento> existente = postulacionEventosRepositorio
                .findByEventoIdAndArtistaId(eventoId, artistaId);


        if (existente.isPresent()) {
            PostulacionEvento existenteSolicitud = existente.get();
            if (existenteSolicitud.getTipoSolicitud() == TipoSolicitud.postulacion) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya te has postulado para este evento.");
            } else if (existenteSolicitud.getTipoSolicitud() == TipoSolicitud.oferta) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "El artista ya ha recibido una oferta para este evento.");
            } else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una oferta/postulación previa.");
            }
        }

        PostulacionEvento pe = new PostulacionEvento();
        pe.setEvento(evento);
        pe.setArtista(artista);
        pe.setTipoSolicitud(tipo);
        pe.setEstadoPostulacion(EstadoPostulacion.pendiente);
        postulacionEventosRepositorio.save(pe);
    }


    public List<PostulacionDTO> listarOfertasArtista(Integer artistaId) {
        LocalDate hoy = LocalDate.now();
        return postulacionEventosRepositorio
                .findPostulacionesWithEventoAndArtista(artistaId, List.of(TipoSolicitud.postulacion, TipoSolicitud.oferta))
                .stream()
                .filter(pe -> !pe.getEvento().getFechaEvento().isBefore(hoy))// ✅ solo eventos hoy o en el pasado
                .map(pe -> new PostulacionDTO(
                        pe.getId(),
                        pe.getEvento().getNombre_evento(),
                        null,
                        pe.getEvento().getPromotor().getNombrePromotor(),
                        pe.getEvento().getImagen_evento(),
                        pe.getEvento().getSala().getNombre(),
                        pe.getEstadoPostulacion(),
                        pe.getFechaPostulacion(),
                        pe.getTipoSolicitud(),
                        pe.getEvento().getFechaEvento()
                ))
                .collect(Collectors.toList());
    }



    /**Lista las solicitudes del promotor en perfil promotores
     * (del promotor arl artista y del artista al promotor)*/
    public List<PostulacionDTO> listarSolicitudesPromotor(Integer promotorId) {
        LocalDate hoy = LocalDate.now();
        return postulacionEventosRepositorio.findPostulacionesWithEventoAndPromotor(promotorId, List.of(TipoSolicitud.postulacion, TipoSolicitud.oferta))
                .stream()
                .filter(pe -> !pe.getEvento().getFechaEvento().isBefore(hoy))
                .map(pe -> new PostulacionDTO(
                        pe.getId(),
                        pe.getEvento().getNombre_evento(),
                        pe.getArtista().getNombreArtista(),
                        null,
                        pe.getEvento().getImagen_evento(),
                        pe.getEvento().getSala().getNombre(),
                        pe.getEstadoPostulacion(),
                        pe.getFechaPostulacion(),
                        pe.getTipoSolicitud(),
                        pe.getEvento().getFechaEvento()
                ))
                .collect(Collectors.toList());
    }


    @Transactional
    public void actualizarEstado(Integer id, EstadoPostulacion nuevoEstado) {
        PostulacionEvento pe = postulacionEventosRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Postulación no encontrada"));
        pe.setEstadoPostulacion(nuevoEstado);
        pe.setFechaRespuesta(LocalDate.now());
        if (nuevoEstado == EstadoPostulacion.aceptado) {
            Eventos evento = pe.getEvento();

            // Confirmar evento si está en revisión
            if (evento.getEstado() == Estado.en_revision) {
                evento.setEstado(Estado.confirmado);
            }

            // Asignar artista al evento si no está ya asignado
            Artistas artista = pe.getArtista();
            if (!evento.getArtistasAsignados().contains(artista)) {
                evento.getArtistasAsignados().add(artista);
            }

            eventosRepositorio.save(evento); // Asegúrate de tener este repositorio inyectado
        }
        postulacionEventosRepositorio.save(pe);
    }


}

