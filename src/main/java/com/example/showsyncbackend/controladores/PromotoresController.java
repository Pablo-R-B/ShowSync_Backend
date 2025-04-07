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
public class PromotoresController {
    private final PromotoresServicio promotoresServicio;



    @GetMapping("/{id}")
    public ResponseEntity<Promotores> obtenerPerfilPromotor(@PathVariable Integer id) {
        return ResponseEntity.ok(promotoresServicio.obtenerPromotorPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<Promotores>> listarPromotores() {
        return ResponseEntity.ok(promotoresServicio.listarPromotores());
    }

    @GetMapping("/{id}/eventos")
    public ResponseEntity<List<Eventos>> listarEventosPorPromotor(@PathVariable Integer id) {
        return ResponseEntity.ok(promotoresServicio.listarEventosDePromotor(id));
    }

    @PostMapping("/{id}/solicitar-sala")
    public ResponseEntity<String> solicitarSala(@PathVariable Integer id, @RequestParam String sala) {
        promotoresServicio.solicitarSala(id, sala);
        return ResponseEntity.ok("Solicitud de sala enviada exitosamente");
    }
}

