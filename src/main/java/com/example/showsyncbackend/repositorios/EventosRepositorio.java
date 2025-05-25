package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface EventosRepositorio extends JpaRepository<Eventos,Integer> {


    List<Eventos> findByEstado(Estado estado);

    List<Eventos> findByPromotorId(Integer promotorId);





    List<Eventos> findByPromotor(Promotores promotor);

    @Query("SELECT DISTINCT g.nombre FROM Eventos e JOIN e.generosMusicales g")
    List<String> findDistinctGeneros();



    @Query("SELECT DISTINCT e.estado FROM Eventos e")
    List<String> findDistinctEstados();


    @Query("SELECT COUNT(e) > 0 FROM Eventos e WHERE e.sala_id.id = :salaId AND e.fecha_evento = :fecha")
    boolean existsBySalaAndFecha(Integer salaId, java.time.LocalDate fecha);
}
