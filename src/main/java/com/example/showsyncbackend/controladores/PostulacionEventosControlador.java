package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.modelos.PostulacionEvento;
import com.example.showsyncbackend.servicios.PostulacionEventosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

}
