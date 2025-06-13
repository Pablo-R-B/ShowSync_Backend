package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Conversacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ConversacionRepositorio extends JpaRepository<Conversacion, Long> {
    @Query("SELECT c FROM Conversacion c WHERE " +
            "(c.artistaId = :id1 AND c.promotorId = :id2) OR " +
            "(c.artistaId = :id2 AND c.promotorId = :id1)")
    Optional<Conversacion> findByParticipantes(@Param("id1") Long id1, @Param("id2") Long id2);

    // Los siguientes métodos ya no son estrictamente necesarios para getOrCreateConversation
    // si usas findByParticipantes, pero puedes mantenerlos si se usan en otros lugares.
    Optional<Conversacion> findByArtistaIdAndPromotorId(Long artistaId, Long promotorId);

    Optional<Conversacion> findByPromotorIdAndArtistaId(Long promotorId, Long artistaId);
}
