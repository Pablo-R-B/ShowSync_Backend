package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Artistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistasRepositorio extends JpaRepository<Artistas, Integer> {

//    @Query("SELECT a FROM Artistas a JOIN FETCH a.generos")
//    List<Artistas> findAllWithGeneros();

//    @Query("SELECT DISTINCT a FROM Artistas a LEFT JOIN FETCH a.generos")
//    List<Object[]> findAllWithGeneros();

//    @Query("SELECT a, g FROM Artistas a JOIN a.generos g")
//    List<Object[]> findAllWithGeneros();

    @Query("SELECT a.id, a.nombreArtista, g.nombre FROM Artistas a JOIN a.generos g")
    List<Object[]> findAllWithGeneros();



}
