package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.modelos.Artistas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistasRepositorio extends JpaRepository<Artistas, Integer> {

    @Query("SELECT a.id, a.nombreArtista, a.imagenPerfil,g.nombre FROM Artistas a JOIN a.generosMusicales g")
    Page<Object[]> findAllWithGeneros(Pageable pageable);

    @Query("SELECT a.id, a.nombreArtista, a.imagenPerfil, g.nombre " +
            "FROM Artistas a JOIN a.generosMusicales g " +
            "WHERE g.nombre = :genero " +
            "AND (:termino IS NULL OR LOWER(a.nombreArtista) LIKE LOWER(CONCAT('%', :termino, '%')))")
    Page<Object[]> findArtistasByGenero(
            @Param("genero") String genero,
            @Param("termino") String termino,
            Pageable pageable
    );

    @Query("SELECT a.id, a.nombreArtista, a.imagenPerfil, g.nombre " +
            "FROM Artistas a JOIN a.generosMusicales g " +
            "WHERE LOWER(a.nombreArtista) LIKE LOWER(CONCAT('%', :termino, '%'))")
    Page<Object[]> findArtistasByNombre(
            @Param("termino") String termino,
            Pageable pageable
    );



}
