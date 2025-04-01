package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.repositorios.GenerosMusicalesRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GenerosMusicalesServicio {

    private GenerosMusicalesRepositorio generosMusicalesRepositorio;

    public List<String> mostrarTodos() {
        return generosMusicalesRepositorio.findAllWithGeneros();

    }
}
