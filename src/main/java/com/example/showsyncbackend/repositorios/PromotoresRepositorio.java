package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.Promotores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotoresRepositorio extends JpaRepository<Promotores,Integer> {
    Optional<Promotores> findByUsuarioId(Integer usuarioId);


    Optional<Promotores> findById(Integer id);


}
