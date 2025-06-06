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
    List<PostulacionEvento> findByArtista_IdAndTipoSolicitud(
            Integer artistaId,
            TipoSolicitud tipoSolicitud
    );

    List<PostulacionEvento> findByArtistaId(Integer artistaId);

    List<PostulacionEvento> findByEvento_Promotor_IdAndTipoSolicitud(
            Integer promotorId,
            TipoSolicitud tipoSolicitud
    );

//    List<PostulacionEvento> findByEvento_Promotor_IdAndTipoSolicitudIn(
//            Integer promotorId,
//            List<TipoSolicitud> tiposSolicitud
//    );

    @Query("""
    SELECT pe FROM PostulacionEvento pe
    JOIN FETCH pe.evento e
    JOIN FETCH e.promotor p
    JOIN FETCH e.sala s
    JOIN FETCH pe.artista a
    WHERE p.id = :promotorId
    AND pe.tipoSolicitud IN :tiposSolicitud
""")
    List<PostulacionEvento> findPostulacionesWithEventoAndArtista(
            @Param("promotorId") Integer promotorId,
            @Param("tiposSolicitud") List<TipoSolicitud> tiposSolicitud
    );

    @Query("""
    SELECT pe FROM PostulacionEvento pe
    JOIN FETCH pe.evento e
    JOIN FETCH e.promotor p
    JOIN FETCH e.sala s
    WHERE pe.artista.id = :artistaId
    AND pe.tipoSolicitud IN :tipos
""")
    List<PostulacionEvento> findPostulacionesConDetallesByArtistaId(
            @Param("artistaId") Integer artistaId,
            @Param("tipos") List<TipoSolicitud> tipos
    );

    Optional<PostulacionEvento> findByEventoIdAndArtistaId(Integer eventoId, Integer artistaId);




    List<PostulacionEvento> findByEvento_IdAndTipoSolicitudAndEstadoPostulacion(
            Integer eventoId,
            TipoSolicitud tipoSolicitud,
            EstadoPostulacion estadoPostulacion);





}
