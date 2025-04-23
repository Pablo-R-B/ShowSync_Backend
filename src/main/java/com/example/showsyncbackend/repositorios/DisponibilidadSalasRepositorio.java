package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.modelos.DisponibilidadSalas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DisponibilidadSalasRepositorio extends JpaRepository<DisponibilidadSalas, Integer> {
    List<DisponibilidadSalas> findBySalaId(Integer salaId);
    List<DisponibilidadSalas> findByFechaAndSalaId(LocalDate fecha, Integer salaId);
    List<DisponibilidadSalas> findByFechaBetweenAndSalaId(LocalDate fechaInicio, LocalDate fechaFin, Integer salaId);
}
