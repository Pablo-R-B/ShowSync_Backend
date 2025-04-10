package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.TokenRecuperacion;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRecuperacionRepositorio extends JpaRepository<TokenRecuperacion, Integer> {

    Optional<TokenRecuperacion> findByToken(String token);


    Optional<TokenRecuperacion> findByUsuarioId(Integer usuarioId);

    @Transactional
    @Modifying
    @Query("DELETE FROM TokenRecuperacion t WHERE t.usuario.email = :email")
    void deleteByUsuarioEmail(String email);



}