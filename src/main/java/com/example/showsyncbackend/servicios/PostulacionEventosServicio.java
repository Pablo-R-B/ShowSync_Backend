package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.PostulacionDTO;
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
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostulacionEventosServicio {
    private PostulacionEventosRepositorio postulacionEventosRepositorio;
    private EventosRepositorio eventosRepository;
    private ArtistasRepositorio artistasRepositorio;

    @Transactional
    public void nuevaSolicitud(Integer eventoId, Integer artistaId, TipoSolicitud tipo){
        Eventos evento = eventosRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        if (evento.getFechaEvento().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede crear una solicitud para un evento pasado.");
        }

        Artistas artista = artistasRepositorio.findById(artistaId)
                .orElseThrow(() -> new EntityNotFoundException("Artista no encontrado"));

        PostulacionEvento pe = new PostulacionEvento();
        pe.setEvento(evento);
        pe.setArtista(artista);
        pe.setTipoSolicitud(tipo);
        pe.setEstadoPostulacion(EstadoPostulacion.pendiente);
        postulacionEventosRepositorio.save(pe);
    }


    public List<PostulacionDTO> listarOfertasArtista(Integer artistaId) {
        return postulacionEventosRepositorio
                .findPostulacionesConDetallesByArtistaId(artistaId, List.of(TipoSolicitud.postulacion, TipoSolicitud.oferta))
                .stream()
                .map(pe -> new PostulacionDTO(
                        pe.getId(),
                        pe.getEvento().getNombre_evento(),
                        null,
                        pe.getEvento().getPromotor().getNombrePromotor(),
                        pe.getEvento().getImagen_evento(),
                        pe.getEvento().getSala().getNombre(),
                        pe.getEstadoPostulacion(),
                        pe.getFechaPostulacion(),
                        pe.getTipoSolicitud()
                ))
                .collect(Collectors.toList());
    }



    /**Lista las solicitudes del promotor en perfil promotores
     * (del promotor arl artista y del artista al promotor)*/
    public List<PostulacionDTO> listarSolicitudesPromotor(Integer promotorId) {
        return postulacionEventosRepositorio.findPostulacionesWithEventoAndArtista(promotorId, List.of(TipoSolicitud.postulacion, TipoSolicitud.oferta))
                .stream()
                .map(pe -> new PostulacionDTO(
                        pe.getId(),
                        pe.getEvento().getNombre_evento(),
                        pe.getArtista().getNombreArtista(),
                        null,
                        pe.getEvento().getImagen_evento(),
                        pe.getEvento().getSala().getNombre(),
                        pe.getEstadoPostulacion(),
                        pe.getFechaPostulacion(),
                        pe.getTipoSolicitud()
                ))
                .collect(Collectors.toList());
    }




    public void actualizarEstado(Integer id, EstadoPostulacion nuevoEstado) {
        PostulacionEvento pe = postulacionEventosRepositorio.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Postulación no encontrada"));
        pe.setEstadoPostulacion(nuevoEstado);
        pe.setFechaRespuesta(LocalDate.now());
        postulacionEventosRepositorio.save(pe);
    }


}

