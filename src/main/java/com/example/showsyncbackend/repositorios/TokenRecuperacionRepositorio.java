package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.TokenRecuperacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacionRepositorio extends JpaRepository<TokenRecuperacion, Integer> {

    Optional<TokenRecuperacion> findByToken(String token);

    Optional<TokenRecuperacion> findByUsuarioId(Integer usuarioId);
}
