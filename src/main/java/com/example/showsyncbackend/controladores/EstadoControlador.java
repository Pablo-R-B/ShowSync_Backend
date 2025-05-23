package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.servicios.EstadoServicio;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/estado")
@AllArgsConstructor
public class EstadoControlador {

    private EstadoServicio estadoServicio;

    /**
     * Endpoint para obtener todos los estados
     * @return Lista de estados
     */

    @GetMapping
    public List<String> getEstados() {
        return estadoServicio.obtenerTodos();
    }
}
