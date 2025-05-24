package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ActualizarEstadoPostulacionDTO;
import com.example.showsyncbackend.dtos.PostulacionDTO;
import com.example.showsyncbackend.enumerados.TipoSolicitud;
import com.example.showsyncbackend.modelos.PostulacionEvento;
import com.example.showsyncbackend.servicios.PostulacionEventosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/postulacion")

public class PostulacionEventosControlador {
    @Autowired
    private PostulacionEventosServicio  postulacionEventosServicio;

    @PostMapping("/{eventoId}/solicitud")
    public ResponseEntity<Void> crearSolitud(
            @PathVariable Integer eventoId,
            @RequestBody(required = false) Map<String, Integer> body,
            @RequestHeader("X-User-Role") String rol
    ) {

        Integer artistaIdParam = body != null ? body.get("artistaId") : null;
        Integer artistaId;
        TipoSolicitud tipo;

        if ("PROMOTOR".equalsIgnoreCase(rol)) {
            if (artistaIdParam == null) {
                throw new IllegalArgumentException("Promotor debe especificar artistaId");
            }
            artistaId = artistaIdParam;
            tipo = TipoSolicitud.oferta;
        } else {
            // En este caso, el artistaId podría venir en el body
            if (artistaIdParam == null) {
                throw new IllegalArgumentException("Artista debe indicar su artistaId");
            }
            artistaId = artistaIdParam;
            tipo = TipoSolicitud.postulacion;
        }

        postulacionEventosServicio.nuevaSolicitud(eventoId, artistaId, tipo);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }



    @GetMapping("/artista/{artistaId}")
    public List<PostulacionDTO> getEventosArtista(@PathVariable Integer artistaId) {
        return postulacionEventosServicio.listarOfertasArtista(artistaId);
    }
    @GetMapping("/promotor/{promotorId}")
    public List<PostulacionDTO> getEventosPromotor(@PathVariable Integer promotorId) {
        return postulacionEventosServicio.listarSolicitudesPromotor(promotorId);
    }
    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody ActualizarEstadoPostulacionDTO req) {
        postulacionEventosServicio.actualizarEstado(id, req.getNuevoEstado());
        return ResponseEntity.noContent().build();
    }






}
