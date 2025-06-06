package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.GenerosMusicalesDTO;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.repositorios.GenerosMusicalesRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenerosMusicalesServicio {

    private GenerosMusicalesRepositorio generosMusicalesRepositorio;

    public List<String> mostrarTodos() {
        return generosMusicalesRepositorio.findAllWithGeneros();
    }

    // Listar géneros con id y nombre en un DTO
    public List<GenerosMusicalesDTO> listarGenerosConId() {
        List<GenerosMusicales> generos = generosMusicalesRepositorio.findAll();
        return generos.stream()
                .map(g -> new GenerosMusicalesDTO(g.getId(), g.getNombre()))
                .collect(Collectors.toList());
    }

    // Crear género
    public GenerosMusicalesDTO crearGenero(GenerosMusicalesDTO dto) {
        if (generosMusicalesRepositorio.existsByNombre(dto.getNombre())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un género con ese nombre.");
        }

        GenerosMusicales genero = new GenerosMusicales();
        genero.setNombre(dto.getNombre());

        generosMusicalesRepositorio.save(genero);

        return new GenerosMusicalesDTO(genero.getId(), genero.getNombre());
    }







    // Actualizar género
    public GenerosMusicalesDTO actualizarGenero(GenerosMusicalesDTO genero) {
        GenerosMusicales entidad = GenerosMusicales.builder()
                .id(genero.getId().intValue())
                .nombre(genero.getNombre())
                .build();

        GenerosMusicales actualizado = generosMusicalesRepositorio.save(entidad);
        return new GenerosMusicalesDTO(actualizado.getId(), actualizado.getNombre());
    }

    // Eliminar género
    public void eliminarGenero(Long id) {
        if (id > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("ID demasiado grande para ser convertido a Integer");
        }
        generosMusicalesRepositorio.deleteById(id.intValue());
    }
}

