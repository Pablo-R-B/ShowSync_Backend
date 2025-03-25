package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Artistas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistasRepositorio extends JpaRepository<Artistas, Integer> {
}
