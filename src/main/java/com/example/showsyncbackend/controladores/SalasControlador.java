package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.*;
import com.example.showsyncbackend.servicios.EventosServicio;
import com.example.showsyncbackend.servicios.SalasServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/crear", consumes = "multipart/form-data")
    public ResponseEntity<SalaDTO> crearSala(
            @RequestPart("data") CrearSalaRequestDTO request,
            @RequestPart("imagen") MultipartFile imagenArchivo) {
        return ResponseEntity.ok(salasServicio.crearSala(request, imagenArchivo));
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @PutMapping(value = "/editar/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<SalaDTO> editarSala(
            @PathVariable Integer id,
            @RequestPart("sala") CrearSalaRequestDTO request,
            @RequestPart(value = "imagenArchivo", required = false) MultipartFile imagenArchivo) {
        return ResponseEntity.ok(salasServicio.editarSala(id, request, imagenArchivo));
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

    // Endpoints paginados actualizados
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/todas")
    public ResponseEntity<RespuestaPaginacionDTO<SalaDTO>> obtenerTodasLasSalas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(value = "termino", required = false) String termino) {
        return ResponseEntity.ok(salasServicio.obtenerTodasLasSalas(page, size, sortField, sortDirection, termino));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-paginado")
    public ResponseEntity<RespuestaPaginacionDTO<SalaDTO>> buscarSalasConPaginacion(
            @RequestParam("filtro") String filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(salasServicio.buscarSalasConPaginacion(filtro, page, size, sortField, sortDirection));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/filtrar")
    public ResponseEntity<RespuestaPaginacionDTO<SalaDTO>> filtrarPorCapacidad(
            @RequestParam("capacidadMinima") Integer capacidadMinima,
            @RequestParam("capacidadMaxima") Integer capacidadMaxima,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "capacidad") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection,
            @RequestParam(value = "termino", required = false) String termino) {
        return ResponseEntity.ok(salasServicio.filtrarPorCapacidad(
                capacidadMinima, capacidadMaxima, page, size, sortField, sortDirection, termino));
    }

    // Endpoints de búsqueda por ciudad/provincia paginados
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-ciudad-paginado")
    public ResponseEntity<RespuestaPaginacionDTO<SalaDTO>> buscarSalasPorCiudadConPaginacion(
            @RequestParam("ciudad") String ciudad,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorCiudadConPaginacion(
                ciudad, page, size, sortField, sortDirection));
    }

    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar-por-provincia-paginado")
    public ResponseEntity<RespuestaPaginacionDTO<SalaDTO>> buscarSalasPorProvinciaConPaginacion(
            @RequestParam("provincia") String provincia,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(defaultValue = "nombre") String sortField,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok(salasServicio.buscarSalasPorProvinciaConPaginacion(
                provincia, page, size, sortField, sortDirection));
    }

    // Endpoints sin paginación (opcionales, considerar eliminarlos si no son necesarios)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'PROMOTOR')")
    @GetMapping("/buscar")
    public ResponseEntity<List<SalaDTO>> buscarSalas(@RequestParam("filtro") String filtro) {
        return ResponseEntity.ok(salasServicio.buscarSalas(filtro));
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

    // Resto de endpoints no relacionados con paginación
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
    public List<SalaEstadoCantidadDTO> obtenerCantidadReservasPorSalaYEstado() {
        return salasServicio.obtenerCantidadReservasPorSalaYEstadoDTO();
    }
}