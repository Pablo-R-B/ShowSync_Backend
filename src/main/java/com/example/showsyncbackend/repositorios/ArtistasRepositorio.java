package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Artistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistasRepositorio extends JpaRepository<Artistas, Integer> {

    @Query("SELECT a.id, a.nombreArtista, g.nombre FROM Artistas a JOIN a.generosMusicales g")
    List<Object[]> findAllWithGeneros();


}
