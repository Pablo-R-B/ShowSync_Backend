package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Salas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalasRepositorio extends JpaRepository<Salas, Integer> {

    @Query("SELECT s FROM Salas s WHERE " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.direccion) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.provincia) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "CAST(s.capacidad AS string) LIKE CONCAT('%', :filtro, '%')")
    List<Salas> buscarSalas(@Param("filtro") String filtro);
}
