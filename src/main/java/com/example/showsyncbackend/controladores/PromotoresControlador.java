package com.example.showsyncbackend.controladores;

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
    public ResponseEntity<Promotores> getPromotorByUsuarioId(@PathVariable Integer idUsuario) {
        Promotores promotor = promotoresServicio.obtenerPromotorPorUsuarioId(idUsuario);
        return ResponseEntity.ok(promotor);
    }




}