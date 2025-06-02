package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.GenerosMusicalesDTO;
import com.example.showsyncbackend.servicios.GenerosMusicalesServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/genero")
@AllArgsConstructor
public class GenerosMusicalesControlador {

    private final GenerosMusicalesServicio generosMusicalesServicio;

    @GetMapping("/listar-generos")
    public ResponseEntity<List<GenerosMusicalesDTO>> listarGeneros() {
        List<GenerosMusicalesDTO> generos = generosMusicalesServicio.listarGenerosConId();
        return ResponseEntity.ok(generos);
    }
}
