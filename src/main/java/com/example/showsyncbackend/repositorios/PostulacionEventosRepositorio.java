package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.PostulacionEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostulacionEventosRepositorio extends JpaRepository<PostulacionEvento, Integer> {
    List<PostulacionEvento> findByArtistaId(Integer artistaId);

    List<PostulacionEvento> findByArtistaUsuarioId(Integer usuarioId);


}
