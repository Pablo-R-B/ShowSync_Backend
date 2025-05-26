package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.GenerosMusicalesDTO;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.servicios.GenerosMusicalesServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/genero-admin")  // <-- ruta distinta
@AllArgsConstructor
public class GenerosMusicalesAdminControlador {

    private final GenerosMusicalesServicio generosMusicalesServicio;

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @GetMapping("/listar")
    public ResponseEntity<List<GenerosMusicalesDTO>> listarGenerosConId() {
        List<GenerosMusicalesDTO> generos = generosMusicalesServicio.listarGenerosConId();
        return ResponseEntity.ok(generos);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PostMapping("/crear")
    public ResponseEntity<GenerosMusicalesDTO> crearGenero(@RequestBody GenerosMusicalesDTO generoDTO) {
        GenerosMusicalesDTO creado = generosMusicalesServicio.crearGenero(generoDTO);
        return ResponseEntity.ok(creado);
    }


    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @PutMapping("/actualizar")
    public ResponseEntity<GenerosMusicalesDTO> actualizarGenero(@RequestBody GenerosMusicalesDTO generoDTO) {
        GenerosMusicalesDTO actualizado = generosMusicalesServicio.actualizarGenero(generoDTO);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(actualizado);
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarGenero(@PathVariable Long id) {
        generosMusicalesServicio.eliminarGenero(id);
        return ResponseEntity.noContent().build();
    }
}
