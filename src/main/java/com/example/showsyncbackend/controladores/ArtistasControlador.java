package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
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
    public ResponseEntity<?> artistasCatalogo() {
        try {
            List<Map<String, Object>> lista = artistasServicio.obtenerArtistasConGeneros();
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            System.err.println("Error en el endpoint /listar-artistas");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener el catálogo de artistas. Detalles: " + e.getMessage());
        }
    }

    @GetMapping("/artistas-por-genero")
    public ResponseEntity<List<ArtistasCatalogoDTO>> artistasPorGenero(@RequestParam("genero") String genero) {
        List<ArtistasCatalogoDTO> artistasFiltrados = artistasServicio.artistasPorGenero(genero);
        if(artistasFiltrados.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(artistasFiltrados);

    }

    @GetMapping("/buscar")

    public ResponseEntity<List<ArtistasCatalogoDTO>> buscarArtistasPorNombre(
            @RequestParam("termino") String termino) {
        List<ArtistasCatalogoDTO> artistasFiltrados = artistasServicio.buscarArtistasPorNombre(termino);
        return ResponseEntity.ok(artistasFiltrados);

        // ... misma lógica pero con DTO

    }


    @GetMapping("/todos")
    public ResponseEntity<?> obtenerArtistas() {
        List<Artistas> artistas = artistasServicio.findAll();
        return ResponseEntity.ok(artistas);
    }


}
