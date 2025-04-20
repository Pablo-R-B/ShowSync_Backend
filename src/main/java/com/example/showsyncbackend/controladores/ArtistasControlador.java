package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.servicios.ArtistasServicio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/artistas")
@AllArgsConstructor
public class ArtistasControlador {

    @Autowired
    private ArtistasServicio artistasServicio;

    @GetMapping("/listar-artistas")
    public ResponseEntity<?> artistasCatalogo(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(value = "termino", required = false) String termino
    ) {
        try {
           RespuestaPaginacionDTO<ArtistasCatalogoDTO> lista = artistasServicio.obtenerArtistasConGeneros(page, size, termino);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            System.err.println("Error en el endpoint /listar-artistas");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el catálogo de artistas. Detalles: " + e.getMessage());
        }
    }

    @GetMapping("/artistas-por-genero")
    public ResponseEntity<RespuestaPaginacionDTO<ArtistasCatalogoDTO>> artistasPorGenero(
            @RequestParam("genero") String genero,
            @RequestParam(value = "termino", required = false) String termino,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        RespuestaPaginacionDTO<ArtistasCatalogoDTO> artistasFiltrados = artistasServicio.artistasPorGenero(genero, termino, page, size);
        return artistasFiltrados.getItems().isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(artistasFiltrados);

    }

//    @GetMapping("/buscar")
//
//    public ResponseEntity<RespuestaPaginacionDTO<ArtistasCatalogoDTO>> buscarArtistasPorNombre(
//            @RequestParam("termino") String termino,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        RespuestaPaginacionDTO<ArtistasCatalogoDTO> artistasFiltrados = artistasServicio.buscarArtistasPorNombre(termino, page, size);
//        return ResponseEntity.ok(artistasFiltrados);
//
//    }


    @GetMapping("/todos")
    public ResponseEntity<?> obtenerArtistas() {
        List<Artistas> artistas = artistasServicio.findAll();
        return ResponseEntity.ok(artistas);
    }


}
