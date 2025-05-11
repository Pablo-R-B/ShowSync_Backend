package com.example.showsyncbackend.servicios;

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

@Service
@AllArgsConstructor
public class PostulacionEventosServicio {
    private PostulacionEventosRepositorio postulacionEventosRepositorio;
    private EventosRepositorio eventosRepository;
    private ArtistasRepositorio artistasRepositorio;

    @Transactional
    public PostulacionEvento nuevaOfertaPormotor(Integer eventoId, Integer artistaId){
        Eventos evento = eventosRepository.findById(eventoId)
                .orElseThrow(() -> new EntityNotFoundException("Evento no encontrado"));

        Artistas artista = artistasRepositorio.findById(artistaId)
                .orElseThrow(() -> new EntityNotFoundException("Artista no encontrado"));

        PostulacionEvento postulacionEvento = new PostulacionEvento();
        postulacionEvento.setEvento(evento);
        postulacionEvento.setArtista(artista);
        postulacionEvento.setTipoSolicitud(TipoSolicitud.oferta);
        postulacionEvento.setEstadoPostulacion(EstadoPostulacion.pendiente);

        return postulacionEventosRepositorio.save(postulacionEvento);
    }
}

