package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.GenerosMusicales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenerosMusicalesRepositorio extends JpaRepository<GenerosMusicales, Integer> {

    @Query("select g.nombre from GenerosMusicales g ")
    List<String> findAllWithGeneros();


    List<GenerosMusicales> findByNombreIn(Set<String> nombres);

    boolean existsByNombre(String nombre);

    Optional<GenerosMusicales> findByNombre(String nombre);
}
