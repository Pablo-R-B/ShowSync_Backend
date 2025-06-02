package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.dtos.UsuarioDTO;
import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;


    @GetMapping
    public ResponseEntity<RespuestaPaginacionDTO<UsuarioDTO>> listarUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String termino,
            @RequestParam(required = false) String rol,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction) {

        // Validación básica de parámetros
        if (page < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El número de página no puede ser negativo");
        }

        if (size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tamaño de página debe ser mayor a 0");
        }

        // Conversión del rol
        Rol rolEnum = null;
        if (rol != null && !rol.isBlank()) {
            try {
                rolEnum = Rol.valueOf(rol.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol inválido. Valores aceptados: ADMINISTRADOR, ARTISTA, PROMOTOR");
            }
        }

        // Limpieza del término de búsqueda
        if (termino != null && termino.isBlank()) {
            termino = null;
        }

        // Obtención de los resultados paginados
        RespuestaPaginacionDTO<UsuarioDTO> respuesta = usuarioServicio.obtenerUsuariosFiltrados(
                page, size, termino, rolEnum, sortField, direction);

        // Manejo de respuesta vacía
        if (respuesta.getItems().isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(respuesta);
        }

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
   public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Integer id) {
       try {
           Usuario usuario = usuarioServicio.obtenerUsuarioPorId(id);
           UsuarioDTO usuarioDTO = new UsuarioDTO(
                   usuario.getId(),
                   usuario.getNombreUsuario(),
                   usuario.getEmail(),
                   usuario.getRol(),
                   usuario.isVerificado(),
                   usuario.getFechaNacimiento(),
                   usuario.getFechaRegistro()
           );
           return ResponseEntity.ok(usuarioDTO);
       } catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
       }
   }



    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = usuarioServicio.crearUsuario(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuario = usuarioServicio.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        try {
            usuarioServicio.eliminarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




    @GetMapping("/contar-usuarios-por-rol")
    public ResponseEntity<Map<String, Long>> contarUsuariosPorRol() {
        Map<String, Long> resultado = usuarioServicio.contarUsuariosPorRol(null);
        return ResponseEntity.ok(resultado);
    }



}