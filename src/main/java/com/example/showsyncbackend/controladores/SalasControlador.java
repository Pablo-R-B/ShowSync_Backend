package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.servicios.EventosServicio;
import com.example.showsyncbackend.servicios.SalasServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.showsyncbackend.dtos.EventosDTO;


import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/salas")
@RequiredArgsConstructor
public class SalasControlador {

    private final SalasServicio salasServicio;
    private final EventosServicio eventosServicio;

    // Solo adm pueden crear, editar y eliminar salas
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PostMapping("/crear")
    public ResponseEntity<SalaDTO> crearSala(@RequestBody CrearSalaRequestDTO request) {
        return ResponseEntity.ok(salasServicio.crearSala(request));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<SalaDTO> editarSala(@PathVariable Integer id, @RequestBody CrearSalaRequestDTO request) {
        return ResponseEntity.ok(salasServicio.editarSala(id, request));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarSala(@PathVariable Integer id) {
        salasServicio.eliminarSala(id);
        return ResponseEntity.noContent().build();
    }

    // Solo adm y promotor pueden ver y consultar  salas
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SalaDTO> obtenerSalaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(salasServicio.obtenerSalaPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/todas")
    public ResponseEntity<List<SalaDTO>> obtenerTodasLasSalas() {
        return ResponseEntity.ok(salasServicio.obtenerTodasLasSalas());
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<SalaDTO>> buscarSalas(@RequestParam("filtro") String filtro) {
        return ResponseEntity.ok(salasServicio.buscarSalas(filtro));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/filtrar")
    public ResponseEntity<List<SalaDTO>> filtrarPorCapacidad(
            @RequestParam("capacidadMinima") Integer capacidadMinima,
            @RequestParam("capacidadMaxima") Integer capacidadMaxima) {
        return ResponseEntity.ok(salasServicio.filtrarPorCapacidad(capacidadMinima, capacidadMaxima));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<DisponibilidadSalaDTO>> consultarDisponibilidad(
            @RequestParam Integer salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return ResponseEntity.ok(salasServicio.consultarDisponibilidad(salaId, fechaInicio, fechaFin));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarSala(
            @RequestParam Integer salaId,
            @RequestParam Integer promotorId,
            @RequestParam String nombreEvento,
            @RequestParam String descripcion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        salasServicio.solicitarSala(salaId, promotorId, nombreEvento, descripcion, fecha);
        return ResponseEntity.ok("Solicitud de sala enviada correctamente.");
    }



    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-ciudad")
    public ResponseEntity<List<SalaDTO>> buscarSalasPorCiudad(@RequestParam("ciudad") String ciudad) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorCiudad(ciudad));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-provincia")
    public ResponseEntity<List<SalaDTO>> buscarSalasPorProvincia(@RequestParam("provincia") String provincia) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorProvincia(provincia));
    }




}
