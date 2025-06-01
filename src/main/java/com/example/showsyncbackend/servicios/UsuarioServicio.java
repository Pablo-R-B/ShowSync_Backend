package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.dtos.UsuarioDTO;
import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import com.example.showsyncbackend.utilidades.PaginationUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ArtistasRepositorio artistasRepositorio;



    public RespuestaPaginacionDTO<UsuarioDTO> obtenerUsuariosFiltrados(int page, int size, String termino, Rol rol, String sortField, Sort.Direction direction) {
        Pageable pageable = PaginationUtils.createPageable(page, size, sortField, direction);

        // Limpieza del término de búsqueda (por si viene en blanco)
        if (termino != null && termino.isBlank()) {
            termino = null;
        }

        Page<Usuario> usuariosPage = usuarioRepositorio.buscarUsuariosFiltrados(rol, termino, pageable);

        // Verificar si no hay resultados
        if (usuariosPage.isEmpty()) {
            RespuestaPaginacionDTO<UsuarioDTO> response = new RespuestaPaginacionDTO<>();
            response.setItems(List.of());
            response.setTotalPages(0);
            response.setCurrentPage(page);
            response.setTotalItems(0);
            response.setPageSize(size);
            response.setMensaje("No se encontraron usuarios. Intente con otros datos de búsqueda.");
            return response;
        }

        Page<UsuarioDTO> usuarioDTOPage = usuariosPage.map(usuario -> new UsuarioDTO(
                usuario.getId(),
                usuario.getNombreUsuario(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.isVerificado(),
                usuario.getFechaNacimiento(),
                usuario.getFechaRegistro()
        ));

        RespuestaPaginacionDTO<UsuarioDTO> response = PaginationUtils.toPaginationResponse(usuarioDTOPage);
        response.setMensaje(usuariosPage.getTotalElements() > 0 ? "" : "No se encontraron usuarios. Intente con otros datos de búsqueda.");

        return response;
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
