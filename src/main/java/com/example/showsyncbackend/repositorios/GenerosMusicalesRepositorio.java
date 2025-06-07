package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenerosMusicalesRepositorio extends JpaRepository<GenerosMusicales, Integer> {

    @Query("select g.nombre from GenerosMusicales g ")
    List<String> findAllWithGeneros();


    List<GenerosMusicales> findByNombreIn(Set<String> nombres);

    boolean existsByNombre(String nombre);

    Optional<GenerosMusicales> findByNombre(String nombre);


    @Query("SELECT e FROM Eventos e " +
            "LEFT JOIN FETCH e.artistasAsignados a " + // Fetch artists
            "LEFT JOIN FETCH e.generosMusicales g " +  // Fetch genres
            "WHERE e.id = :eventoId")
    Optional<Eventos> findByIdWithArtistsAndGeneros(@Param("eventoId") Integer eventoId);

}
