package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.servicios.PromotoresServicio;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotores")
@AllArgsConstructor
public class PromotoresControlador {

    private final PromotoresServicio promotoresServicio;


    /**
     * Obtener perfil de promotor por ID
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<Promotores> obtenerPerfilPromotor(@PathVariable Integer id) {
        return ResponseEntity.ok(promotoresServicio.obtenerPromotorPorId(id));
    }

    /**
     * Listar todos los promotores
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Promotores>> listarPromotores() {
        return ResponseEntity.ok(promotoresServicio.listarPromotores());
    }

    /**
     * Listar eventos por promotor
     * @param id
     * @param tipo
     * @return
     */
    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<Eventos>> listarEventosPorPromotor(
            @PathVariable Integer id,
            @RequestParam(required = false) String tipo) {
        return ResponseEntity.ok(promotoresServicio.listarEventosDePromotor(id, tipo));
    }

    /**
     * Crear evento para un promotor
     * @param id
     * @param evento
     * @return
     */
    @PostMapping("/{id}/eventos")
    public ResponseEntity<Eventos> crearEvento(@PathVariable Integer id, @RequestBody Eventos evento) {
        return ResponseEntity.ok(promotoresServicio.crearEvento(id, evento));
    }

    /**
     * Actualizar evento existente
     * @param id
     * @param eventoId
     * @param eventoActualizado
     * @return
     */
    @PutMapping("/{id}/eventos/{eventoId}")
    public ResponseEntity<Eventos> editarEvento(
            @PathVariable Integer id,
            @PathVariable Integer eventoId,
            @RequestBody Eventos eventoActualizado) {
        return ResponseEntity.ok(promotoresServicio.editarEvento(id, eventoId, eventoActualizado));
    }

    /**
     * Eliminar evento por ID
     * @param id
     * @param eventoId
     * @return
     */
    @DeleteMapping("/{id}/eventos/{eventoId}")
    public ResponseEntity<String> eliminarEvento(@PathVariable Integer id, @PathVariable Integer eventoId) {
        promotoresServicio.eliminarEvento(id, eventoId);
        return ResponseEntity.ok("Evento eliminado exitosamente");
    }

    /**
     * Solicitar sala para un evento
     * @param id
     * @param sala
     * @return
     */
    @PostMapping("/{id}/solicitar-sala")
    public ResponseEntity<String> solicitarSala(@PathVariable Integer id, @RequestParam String sala) {
        promotoresServicio.solicitarSala(id, sala);
        return ResponseEntity.ok("Sala solicitada exitosamente");
    }
}