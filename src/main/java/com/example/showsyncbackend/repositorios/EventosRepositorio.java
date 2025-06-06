package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.modelos.Salas;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface EventosRepositorio extends JpaRepository<Eventos,Integer> {


    List<Eventos> findByEstado(Estado estado);

    List<Eventos> findByPromotorId(Integer promotorId);


    List<Eventos> findByPromotor(Promotores promotor);

    @Query("SELECT DISTINCT g.nombre FROM Eventos e JOIN e.generosMusicales g")
    List<String> findDistinctGeneros();

    @Query("SELECT e.sala.nombre, COUNT(e) FROM Eventos e GROUP BY e.sala.nombre")
    List<Object[]> obtenerCantidadReservasPorSala();

    @Query("SELECT e.sala.nombre, e.estado, COUNT(e) FROM Eventos e GROUP BY e.sala.nombre, e.estado")
    List<Object[]> obtenerCantidadReservasPorSalaYEstado();



    @Query("SELECT DISTINCT e.estado FROM Eventos e")
    List<Estado> findDistinctEstados();



    @Query("SELECT COUNT(e) > 0 FROM Eventos e WHERE e.sala.id = :salaId AND e.fechaEvento = :fecha")
    boolean existsBySalaAndFecha(Integer salaId, LocalDate fecha);

    List<Eventos> findByEstadoIn(List<Estado> estados);


    @Query("SELECT e FROM Eventos e " +
            "LEFT JOIN FETCH e.generosMusicales " +
            "LEFT JOIN FETCH e.artistasAsignados")
    List<Eventos> findAllWithLazyCollections();

    @Query("SELECT e FROM Eventos e " +
            "LEFT JOIN FETCH e.generosMusicales " +
            "LEFT JOIN FETCH e.artistasAsignados " +
            "WHERE e.id = :eventoId")
    Optional<Eventos> findByIdWithArtistas(@Param("eventoId") Integer eventoId);


    @Query("SELECT e FROM Eventos e " +
            "LEFT JOIN FETCH e.generosMusicales " +
            "LEFT JOIN FETCH e.artistasAsignados")
    List<Eventos> findAllWithLazyCollections(Pageable pageable);




    List<Eventos> findBySalaAndEstadoInAndFechaEventoBetween(
            Salas sala,
            List<Estado> estados,
            LocalDate desde,
            LocalDate hasta
    );


    List<Eventos> findBySalaAndEstadoIn(Salas sala, List<Estado> estados);


    long countByFechaEventoBeforeAndEstadoNot(LocalDate hoy, Estado estado);

    long countByFechaEventoAfterAndEstadoNotOrFechaEventoEqualsAndEstadoNot(LocalDate hoy, Estado estado, LocalDate hoy1, Estado estado1);

    List<Eventos> findByPromotorIdAndEstado(Integer promotorId, Estado estado);
}
