package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ActualizarEstadoPostulacionDTO;
import com.example.showsyncbackend.dtos.PostulacionDTO;
import com.example.showsyncbackend.modelos.PostulacionEvento;
import com.example.showsyncbackend.servicios.PostulacionEventosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/postulacion")
public class PostulacionEventosControlador {
    @Autowired
    private PostulacionEventosServicio  postulacionEventosServicio;

    @PostMapping("/oferta-promotor")
    public ResponseEntity<Void> ofertaPromotor(@RequestParam Integer eventoId, @RequestParam Integer artistaId) {
        PostulacionEvento ofertaPromotor = postulacionEventosServicio.nuevaOfertaPormotor(eventoId, artistaId);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/artista/{artistaId}")
    public List<PostulacionDTO> getByArtista(@PathVariable Integer artistaId) {
        return postulacionEventosServicio.listarPorArtista(artistaId);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody ActualizarEstadoPostulacionDTO req) {
        postulacionEventosServicio.actualizarEstado(id, req.getNuevoEstado());
        return ResponseEntity.noContent().build();
    }

}
