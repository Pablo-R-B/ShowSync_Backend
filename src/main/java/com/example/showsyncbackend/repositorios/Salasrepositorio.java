package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.Salas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Salasrepositorio extends JpaRepository<Salas, Integer> {
}
