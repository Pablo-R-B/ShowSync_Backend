package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventosRepository extends JpaRepository<Eventos,Integer> {

    List<Eventos> findByPromotor(Promotores promotor);


    List<Eventos> findByPromotorId(Integer id);
}
