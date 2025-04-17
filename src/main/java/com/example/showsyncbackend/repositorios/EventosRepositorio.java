package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface EventosRepositorio extends JpaRepository<Eventos,Integer> {


    List<Eventos> findByEstado(Estado estado);

    List<Eventos> findByPromotorId(Integer promotorId);


    List<Eventos> findByPromotor(Promotores promotor);
}
