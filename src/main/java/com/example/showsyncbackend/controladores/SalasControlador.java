package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.servicios.SalasServicio;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


@RestController
@RequestMapping("/admin/salas")
@Controller
public class SalasControlador {

    private final SalasServicio salasServicio;

    public SalasControlador(SalasServicio salasServicio) {
        this.salasServicio = salasServicio;
    }


    @PostMapping("/crear")
    public ResponseEntity<Salas> crearSala(@RequestBody CrearSalaRequestDTO request) {
        Salas salaCreada = salasServicio.crearSala(request);
        return ResponseEntity.ok(salaCreada);
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Salas> editarSala(@PathVariable Integer id, @RequestBody CrearSalaRequestDTO request) {
        return ResponseEntity.ok(salasServicio.editarSala(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarSala(@PathVariable Integer id) {
        salasServicio.eliminarSala(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Salas> obtenerSalaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(salasServicio.obtenerSalaPorId(id));
    }

    @GetMapping("/todas")
    public ResponseEntity<List<Salas>> obtenerTodasLasSalas() {
        return ResponseEntity.ok(salasServicio.obtenerTodasLasSalas());
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Salas>> buscarSalas(@RequestParam("filtro") String filtro) {
        return ResponseEntity.ok(salasServicio.buscarSalas(filtro));
    }







}


