package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.enumerados.TipoSolicitud;
import com.example.showsyncbackend.modelos.PostulacionEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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

    List<PostulacionEvento> findByEvento_Promotor_IdAndTipoSolicitudIn(
            Integer promotorId,
            List<TipoSolicitud> tiposSolicitud
    );

}
