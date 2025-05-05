package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ArtistaPerfilPruebaDTO;
import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.servicios.ArtistasServicio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        RespuestaPaginacionDTO<ArtistasCatalogoDTO> lista =
                artistasServicio.obtenerArtistasConGeneros(page, size, termino);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/artistas-por-genero")
    public ResponseEntity<RespuestaPaginacionDTO<ArtistasCatalogoDTO>> artistasPorGenero(
            @RequestParam("genero") String genero,
            @RequestParam(value = "termino", required = false) String termino,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        RespuestaPaginacionDTO<ArtistasCatalogoDTO> artistasFiltrados =
                artistasServicio.artistasPorGenero(genero, termino, page, size);
        return artistasFiltrados.getItems().isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(artistasFiltrados);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ArtistasCatalogoDTO> obtenerArtistasPorId(@PathVariable Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido"); // [[4]]
        }
        ArtistasCatalogoDTO artistas= artistasServicio.artistaPorId(id);
        return ResponseEntity.ok(artistas);
    }


}
