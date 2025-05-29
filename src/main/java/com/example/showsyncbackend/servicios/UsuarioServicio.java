package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.UsuarioDTO;
import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ArtistasRepositorio artistasRepositorio;



  public List<UsuarioDTO> obtenerTodosLosUsuarios(int page, int size, String termino) {
      return usuarioRepositorio.findAll().stream()
              .map(usuario -> new UsuarioDTO(
                      usuario.getId(),
                      usuario.getNombreUsuario(),
                      usuario.getEmail(),
                      usuario.getRol(),             // Ajustado al orden correcto
                      usuario.isVerificado(),       // Ajustado al orden correcto
                      usuario.getFechaNacimiento(), // Ajustado al orden correcto
                      usuario.getFechaRegistro()
              ))
              .toList();
  }

    public Usuario obtenerUsuarioPorId(Integer id) {
        return usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public Usuario crearUsuario(Usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public Usuario actualizarUsuario(Integer id, Usuario usuarioActualizado) {
        Usuario usuarioExistente = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuarioExistente.setNombreUsuario(usuarioActualizado.getNombreUsuario());
        usuarioExistente.setEmail(usuarioActualizado.getEmail());
        // Actualiza otros campos según sea necesario
        return usuarioRepositorio.save(usuarioExistente);
    }

    public void eliminarUsuario(Integer id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
        usuarioRepositorio.delete(usuario);
    }


    public List<UsuarioDTO> obtenerUsuariosPorRol(Rol rol) {
        return usuarioRepositorio.findByRol(rol).stream()
                .map(usuario -> new UsuarioDTO(
                        usuario.getId(),
                        usuario.getNombreUsuario(),
                        usuario.getEmail(),
                        usuario.getRol(),
                        usuario.isVerificado(),
                        usuario.getFechaNacimiento(),
                        usuario.getFechaRegistro()
                ))
                .toList();
}
}
