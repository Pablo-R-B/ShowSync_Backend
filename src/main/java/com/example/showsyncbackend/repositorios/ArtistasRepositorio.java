package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.modelos.Artistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistasRepositorio extends JpaRepository<Artistas, Integer> {

    @Query("SELECT a.id, a.nombreArtista, a.imagenPerfil,g.nombre FROM Artistas a JOIN a.generosMusicales g")
    List<Object[]> findAllWithGeneros();

//    @Query("select a.nombreArtista, a.imagenPerfil, g.nombre from Artistas a join a.generosMusicales g where g.nombre = :genero")
//    List<Artistas[]> findArtistasByGenero(@Param("genero") String genero);

    @Query("SELECT a.id, a.nombreArtista, a.imagenPerfil, g.nombre FROM Artistas a JOIN a.generosMusicales g WHERE g.nombre = :genero")
    List<Object[]> findArtistasByGenero(@Param("genero") String genero);



}
