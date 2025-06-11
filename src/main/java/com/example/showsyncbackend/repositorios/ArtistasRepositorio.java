package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.Usuario;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ArtistasRepositorio extends JpaRepository<Artistas, Integer> {
        @Query("""
        SELECT DISTINCT a
        FROM Artistas a
        JOIN FETCH a.generosMusicales g
        """)
    Page<Artistas> findAllWithGeneros(Pageable pageable);

    @Query("""
        SELECT DISTINCT a
        FROM Artistas a
        JOIN FETCH a.generosMusicales g
        WHERE g.nombre = :genero
          AND (:termino IS NULL OR LOWER(a.nombreArtista) LIKE LOWER(CONCAT('%', :termino, '%')))
        """)
    Page<Artistas> findArtistasByGenero(
            @Param("genero") String genero,
            @Param("termino") String termino,
            Pageable pageable
    );

    @Query("""
        SELECT DISTINCT a
        FROM Artistas a
        JOIN FETCH a.generosMusicales g
        WHERE LOWER(a.nombreArtista) LIKE LOWER(CONCAT('%', :termino, '%'))
        """)
    Page<Artistas> findArtistasByNombre(
            @Param("termino") String termino,
            Pageable pageable
    );


    @Query("""
        SELECT DISTINCT a
        FROM Artistas a
        JOIN FETCH a.generosMusicales g
        WHERE a.id = :id
        """)
    Optional<Artistas> findByIdWithGeneros(@Param("id") Integer id);

    Optional<Artistas> findByUsuario_Id(Integer usuarioId);

    // Consulta para obtener los artistas asignados a eventos de un promotor específico
    @EntityGraph(attributePaths = {"generosMusicales"})
    @Query("""
    SELECT DISTINCT a
    FROM Eventos e
    JOIN e.artistasAsignados a
    WHERE e.promotor.id = :promotorId
    """)
    Page<Artistas> findArtistasByPromotorThroughEventos(@Param("promotorId") Integer promotorId, Pageable pageable);



    Optional<Artistas> findByUsuario(Usuario usuario);

    Optional<Artistas> findByNombreArtista(String nombreArtista);

    @Query("SELECT COUNT(e) FROM Eventos e JOIN e.artistasAsignados a WHERE a.id = :artistaId")
    long contarEventosPorArtista(@Param("artistaId") Integer artistaId);

}
