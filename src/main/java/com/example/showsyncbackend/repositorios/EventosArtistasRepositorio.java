package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.EventosArtistas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventosArtistasRepositorio extends JpaRepository<EventosArtistas, Integer> {



}
