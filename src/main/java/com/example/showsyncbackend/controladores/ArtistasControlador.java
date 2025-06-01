package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.ArtistaDTO;
import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.dtos.PromotoresDTO;
import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.servicios.ArtistasServicio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
        try {
            RespuestaPaginacionDTO<ArtistasCatalogoDTO> artistasFiltrados =
                    artistasServicio.artistasPorGenero(genero, termino, page, size);

            // Siempre retornar 200, incluso si la lista está vacía
            return ResponseEntity.ok(artistasFiltrados);

        } catch (Exception e) {
            // Solo retornar error si hay un problema real (ej: error de BD)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/artista/{id}")
    public ResponseEntity<ArtistasCatalogoDTO> obtenerArtistasPorId(@PathVariable Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID inválido"); // [[4]]
        }
        ArtistasCatalogoDTO artistas= artistasServicio.artistaPorId(id);
        return ResponseEntity.ok(artistas);
    }

    @GetMapping("/por-usuario/{usuarioId}")
    public ResponseEntity<Integer> getArtistaIdByUsuario(
            @PathVariable Integer usuarioId) {
        Integer artistaId = artistasServicio.getArtistaIdByUsuarioId(usuarioId);
        return ResponseEntity.ok(artistaId);
    }


    /**
     * Obtiene una lista de artistas asignados a eventos de un promotor específico.
     *
     * @param promotorId El ID del promotor.
     * @return Una lista de DTOs de artistas asignados al promotor.
     */
    @GetMapping("/promotor/{promotorId}")
    public ResponseEntity<Page<ArtistasCatalogoDTO>> getArtistasPorPromotor(
            @PathVariable Integer promotorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ArtistasCatalogoDTO> artistas = artistasServicio.obtenerArtistasPorPromotor(promotorId, pageable);
        return ResponseEntity.ok(artistas);
    }

    @PutMapping("/artista/usuario/{id}")
    public ResponseEntity<Map<String, String>> editarDatosArtista(@PathVariable Integer id, @RequestBody Artistas artista) {
        ArtistaDTO dto = artistasServicio.editarDatosArtista(id, artista);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Perfil actualizado correctamente");

        return ResponseEntity.ok(respuesta);
    }



    /**
     * Listar todos los artistas sin paginación
     * @return Lista de ArtistasCatalogoDTO
     */
    @GetMapping("/imagenes")
    public ResponseEntity<List<String>> obtenerImagenesDeTodosLosArtistas() {
        List<String> imagenes = artistasServicio.obtenerImagenesDeTodosLosArtistas();
        return ResponseEntity.ok(imagenes);
    }



}
