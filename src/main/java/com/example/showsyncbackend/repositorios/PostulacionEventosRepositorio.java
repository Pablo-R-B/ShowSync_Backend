package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.enumerados.EstadoPostulacion;
import com.example.showsyncbackend.enumerados.TipoSolicitud;
import com.example.showsyncbackend.modelos.PostulacionEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostulacionEventosRepositorio extends JpaRepository<PostulacionEvento, Integer> {

    @Query("""
    SELECT pe FROM PostulacionEvento pe
    JOIN FETCH pe.evento e
    JOIN FETCH e.promotor p
    JOIN FETCH e.sala s
    JOIN FETCH pe.artista a
    WHERE e.promotor.id = :promotorId
      AND pe.tipoSolicitud IN :tiposSolicitud
      AND e.fechaEvento >= CURRENT_DATE
""")
    List<PostulacionEvento> findPostulacionesWithEventoAndPromotor(
            @Param("promotorId") Integer promotorId,
            @Param("tiposSolicitud") List<TipoSolicitud> tiposSolicitud
    );



    @Query("""
    SELECT pe FROM PostulacionEvento pe
    JOIN FETCH pe.evento e
    JOIN FETCH e.promotor p
    JOIN FETCH e.sala s
    WHERE pe.artista.id = :artistaId
      AND pe.tipoSolicitud IN :tiposSolicitud
      AND e.fechaEvento >= CURRENT_DATE
""")
    List<PostulacionEvento> findPostulacionesWithEventoAndArtista(
            @Param("artistaId") Integer artistaId,
            @Param("tiposSolicitud") List<TipoSolicitud> tiposSolicitud
    );



    Optional<PostulacionEvento> findByEventoIdAndArtistaId(Integer eventoId, Integer artistaId);




    List<PostulacionEvento> findByEvento_IdAndTipoSolicitudAndEstadoPostulacion(
            Integer eventoId,
            TipoSolicitud tipoSolicitud,
            EstadoPostulacion estadoPostulacion);





}
