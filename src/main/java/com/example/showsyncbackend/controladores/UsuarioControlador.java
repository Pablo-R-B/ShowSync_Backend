package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.UsuarioDTO;
import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> obtenerTodosLosUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size,
            @RequestParam(value = "termino", required = false) String termino) {
        List<UsuarioDTO> usuarios = usuarioServicio.obtenerTodosLosUsuarios(page, size, termino);
        return ResponseEntity.ok(usuarios);
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


    @GetMapping("/rol/{rol}")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuariosPorRol(@PathVariable Rol rol) {
        List<UsuarioDTO> usuarios = usuarioServicio.obtenerUsuariosPorRol(rol);
        return ResponseEntity.ok(usuarios);
    }
}