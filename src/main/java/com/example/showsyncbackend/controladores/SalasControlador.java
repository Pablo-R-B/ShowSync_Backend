package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.servicios.SalasServicio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.List;




@RestController
@RequestMapping("/salas")

@Controller
public class SalasControlador {

    @Autowired
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

    @GetMapping("/filtrar")
    public ResponseEntity<List<Salas>> filtrarPorCapacidad(
            @RequestParam("capacidadMinima") Integer capacidadMinima,
            @RequestParam("capacidadMaxima") Integer capacidadMaxima) {

        List<Salas> salasFiltradas = salasServicio.filtrarPorCapacidad(capacidadMinima, capacidadMaxima);
        return ResponseEntity.ok(salasFiltradas);
    }

    // Endpoint para consultar disponibilidad entre fechas
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<DisponibilidadSalaDTO>> consultarDisponibilidad(
            @RequestParam Integer salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<DisponibilidadSalaDTO> disponibilidades = salasServicio.consultarDisponibilidad(salaId, fechaInicio, fechaFin);
        return ResponseEntity.ok(disponibilidades);
    }


    /**
     * Solicitar sala para un evento
     *
     * @param salaId       ID de la sala
     * @param promotorId   ID del promotor
     * @param nombreEvento  Nombre del evento
     * @param descripcion  Descripción del evento
     * @param fecha        Fecha del evento
     * @return Mensaje de éxito
     */

    @PostMapping("/solicitar")
    public String solicitarSala(
            @RequestParam Integer salaId,
            @RequestParam Integer promotorId,
            @RequestParam String nombreEvento,
            @RequestParam String descripcion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        salasServicio.solicitarSala(salaId, promotorId, nombreEvento, descripcion, fecha);
        return "Solicitud de sala enviada correctamente.";
    }
}
