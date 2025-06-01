package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.PromotoresDTO;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.servicios.PromotoresServicio;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/promotores")
@AllArgsConstructor
public class PromotoresControlador {

    private final PromotoresServicio promotoresServicio;


    /**     * Obtener perfil de promotor por ID
     * @param Id
     * @return
     */
    @GetMapping("/{Id}")
    public ResponseEntity<PromotoresDTO> getPromotorById(@PathVariable Integer Id) {
        PromotoresDTO promotorDTO = promotoresServicio.obtenerPromotorPorId(Id);
        if (promotorDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotor no encontrado");
        }
        return ResponseEntity.ok(promotorDTO);
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
     * Crear un nuevo promotor
     * @param promotor
     * @return
     */
    @PostMapping("/crear")
    public ResponseEntity<Promotores> crearPromotor(@RequestBody Promotores promotor) {
        return ResponseEntity.ok(promotoresServicio.crearPromotor(promotor));
    }

    /**
     * Editar un promotor
     * @param id
     * @param promotor
     * @return
     */
    @PutMapping("/editar/{id}")
    public ResponseEntity<Promotores> editarPromotor(@PathVariable Integer id, @RequestBody Promotores promotor) {
        return ResponseEntity.ok(promotoresServicio.editarPromotor(id, promotor));
    }

    /**Editar con DTO para evitar recursividad**/

    @PutMapping("/promotor/usuario/{id}")
    public ResponseEntity<Map<String, String>> editarDatosPromotor(@PathVariable Integer id, @RequestBody Promotores promotor) {
       PromotoresDTO dto = promotoresServicio.editarDatosPromotor(id, promotor);
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Perfil actualizado correctamente");

        return ResponseEntity.ok(respuesta);
    }

    /**
     * Eliminar un promotor
     * @param id
     * @return
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarPromotor(@PathVariable Integer id) {
        promotoresServicio.eliminarPromotor(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Obtener promotor por idUsuario
     * @param idUsuario
     * @return
     */

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<PromotoresDTO> getPromotorByUsuarioId(@PathVariable Integer idUsuario) {
        PromotoresDTO promotorDTO = promotoresServicio.obtenerPromotorPorUsuarioId(idUsuario);
        return ResponseEntity.ok(promotorDTO);
    }

    @GetMapping("/listar/promotores")
    public Page<PromotoresDTO> obtenerPromotoresPaginados(@RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "6") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return promotoresServicio.obtenerPromotoresPaginados(pageable);
    }


}