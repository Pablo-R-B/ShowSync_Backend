package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.servicios.EventosServicio;
import com.example.showsyncbackend.servicios.SalasServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.example.showsyncbackend.dtos.EventosDTO;

import java.time.LocalDate;
import java.util.Collections;
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

    // Solo adm y promotor pueden ver y consultar salas
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/{id}")
    public ResponseEntity<SalaDTO> obtenerSalaPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(salasServicio.obtenerSalaPorId(id));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/todas")
    public ResponseEntity<List<SalaDTO>> obtenerTodasLasSalas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(value = "termino", required = false) String termino) {
        return ResponseEntity.ok(salasServicio.obtenerTodasLasSalas(page, size, termino));
    }

    // Búsqueda sin paginación (para casos específicos)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<SalaDTO>> buscarSalas(@RequestParam("filtro") String filtro) {
        return ResponseEntity.ok(salasServicio.buscarSalas(filtro));
    }

    // Búsqueda con paginación
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-paginado")
    public ResponseEntity<Page<SalaDTO>> buscarSalasConPaginacion(
            @RequestParam("filtro") String filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(salasServicio.buscarSalasConPaginacion(filtro, page, size));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/filtrar")
    public ResponseEntity<List<SalaDTO>> filtrarPorCapacidad(
            @RequestParam("capacidadMinima") Integer capacidadMinima,
            @RequestParam("capacidadMaxima") Integer capacidadMaxima,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(value = "termino", required = false) String termino) {
        return ResponseEntity.ok(salasServicio.filtrarPorCapacidad(capacidadMinima, capacidadMaxima, page, size, termino));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadSalaDTO> consultarDisponibilidad(
            @RequestParam Integer salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio) {
        DisponibilidadSalaDTO disponibilidad = salasServicio.consultarDisponibilidadPorFecha(salaId, fechaInicio);
        return ResponseEntity.ok(disponibilidad);
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

    // Buscar por ciudad sin paginación
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-ciudad")
    public ResponseEntity<List<SalaDTO>> buscarSalasPorCiudad(@RequestParam("ciudad") String ciudad) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorCiudad(ciudad));
    }

    // Buscar por ciudad con paginación
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-ciudad-paginado")
    public ResponseEntity<Page<SalaDTO>> buscarSalasPorCiudadConPaginacion(
            @RequestParam("ciudad") String ciudad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorCiudadConPaginacion(ciudad, page, size));
    }

    // Buscar por provincia sin paginación
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-provincia")
    public ResponseEntity<List<SalaDTO>> buscarSalasPorProvincia(@RequestParam("provincia") String provincia) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorProvincia(provincia));
    }

    // Buscar por provincia con paginación
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-provincia-paginado")
    public ResponseEntity<Page<SalaDTO>> buscarSalasPorProvinciaConPaginacion(
            @RequestParam("provincia") String provincia,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorProvinciaConPaginacion(provincia, page, size));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/{salaId}/fechas-no-disponibles")
    public ResponseEntity<List<DisponibilidadSalaDTO>> consultarFechasNoDisponibles(@PathVariable Integer salaId) {
        return ResponseEntity.ok(salasServicio.consultarFechasNoDisponibles(salaId));
    }


    @GetMapping("/reservas")
    public List<Object[]> obtenerCantidadReservasPorSala() {
        return salasServicio.obtenerCantidadReservasPorSala();
    }

    @GetMapping("/reservas-estado")
    public List<Object[]> obtenerCantidadReservasPorSalaYEstado() {
        return salasServicio.obtenerCantidadReservasPorSalaYEstado();
    }
}