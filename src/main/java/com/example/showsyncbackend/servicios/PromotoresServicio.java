package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.ArtistaDTO;
import com.example.showsyncbackend.dtos.EvetosPostulacionArtistaDTO;
import com.example.showsyncbackend.dtos.PromotoresDTO;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PromotoresServicio {

    private final EventosRepositorio eventosRepository;
    private final EventosRepositorio eventosRepositorio;
    private final PromotoresRepositorio promotoresRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final CloudinaryService cloudinaryService;


    // Obtener perfil de promotor por ID
    public PromotoresDTO obtenerPromotorPorId(Integer promotorId) {
        Promotores promotor = promotoresRepositorio.findById(promotorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Promotor no encontrado"));

        return PromotoresDTO.builder()
                .id(promotor.getId())
                .usuarioId(promotor.getUsuario() != null ? promotor.getUsuario().getId() : null)
                .nombrePromotor(promotor.getNombrePromotor())
                .descripcion(promotor.getDescripcion())
                .imagenPerfil(promotor.getImagenPerfil())
                .build();
    }

    // Listar todos los promotores
    public List<PromotoresDTO> listarPromotores() {
        return promotoresRepositorio.findAll().stream()
                .map(promotor -> PromotoresDTO.builder()
                        .id(promotor.getId())
                        .usuarioId(promotor.getUsuario() != null ? promotor.getUsuario().getId() : null)
                        .nombrePromotor(promotor.getNombrePromotor())
                        .descripcion(promotor.getDescripcion())
                        .imagenPerfil(promotor.getImagenPerfil())
                        .build())
                .collect(Collectors.toList());
    }

    //  Crear un nuevo promotor
    public Promotores crearPromotor(Promotores promotor) {
        return promotoresRepositorio.save(promotor);
    }

    // Editar un promotor
    public Promotores editarPromotor(Integer id, Promotores promotor) {
        Promotores promotorExistente = promotoresRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
        promotorExistente.setUsuario(promotor.getUsuario());
        promotorExistente.setNombrePromotor(promotor.getNombrePromotor());
        promotorExistente.setDescripcion(promotor.getDescripcion());
        return promotoresRepositorio.save(promotorExistente);
    }


    // Eliminar un promotor
    public void eliminarPromotor(Integer id) {
        Promotores promotor = promotoresRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
        List<Eventos> eventos = eventosRepository.findByPromotor(promotor);
        if (!eventos.isEmpty()) {
            throw new RuntimeException("No se puede eliminar el promotor porque tiene eventos asociados");
        }
        promotoresRepositorio.delete(promotor);
    }

    // Obtener promotor por ID de usuario
public PromotoresDTO obtenerPromotorPorUsuarioId(Integer usuarioId) {
    Promotores promotor = promotoresRepositorio.findByUsuarioId(usuarioId)
            .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));

    return PromotoresDTO.builder()
            .id(promotor.getId())
            .usuarioId(promotor.getUsuario().getId())
            .nombrePromotor(promotor.getNombrePromotor())
            .descripcion(promotor.getDescripcion())
            .imagenPerfil(promotor.getImagenPerfil())
            .build();
}



    public Promotores getByUsuario(Usuario usuario) {
        return promotoresRepositorio.findByUsuario(usuario)
                .orElse(null);
    }

    public List<EvetosPostulacionArtistaDTO> obtenerEventosPorUsuarioId(Integer usuarioId) {
        // 1) Obtengo el promotor
        Promotores promotor = promotoresRepositorio.findByUsuarioId(usuarioId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Promotor no encontrado para usuario " + usuarioId)
                );

        // 2) Busco sus eventos
        return eventosRepositorio.findByPromotorId(promotor.getId())
                .stream()
                .map(evt -> new EvetosPostulacionArtistaDTO(evt.getId(), evt.getNombre_evento()))
                .collect(Collectors.toList());
    }


    //Paginación de promotores
    public Page<PromotoresDTO> obtenerPromotoresPaginados(Pageable pageable) {
        return promotoresRepositorio.findAll(pageable)
                .map(promotor -> PromotoresDTO.builder()
                        .id(promotor.getId())
                        .usuarioId(promotor.getUsuario().getId())
                        .nombrePromotor(promotor.getNombrePromotor())
                        .descripcion(promotor.getDescripcion())
                        .imagenPerfil(promotor.getImagenPerfil())
                        .build());
    }

    public Promotores obtenerPromotorPorIdEntity(Integer id) {
        return promotoresRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado con ID: " + id));
    }


    // Este método permite editar los datos de un promotor y también crea un nuevo promotor si no existe.
    @Transactional
    public PromotoresDTO editarDatosPromotor(Integer usuarioId, Promotores datos, MultipartFile imagenArchivo) throws IOException {

        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Promotores promotor = promotoresRepositorio.findByUsuarioId(usuarioId)
                .orElse(null);

        if (promotor == null) {
            promotor = new Promotores();
            promotor.setUsuario(usuario);
        } else {
            promotor.setUsuario(usuario);
        }

        promotor.setNombrePromotor(datos.getNombrePromotor());
        promotor.setDescripcion(datos.getDescripcion());

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            // Subir imagen a Cloudinary
            String urlImagen = cloudinaryService.uploadFile(imagenArchivo);
            promotor.setImagenPerfil(urlImagen);
        }

        Promotores guardado = promotoresRepositorio.save(promotor);

        PromotoresDTO dto = new PromotoresDTO();
        dto.setId(guardado.getId());
        dto.setUsuarioId(usuarioId);
        dto.setNombrePromotor(guardado.getNombrePromotor());
        dto.setDescripcion(guardado.getDescripcion());
        dto.setImagenPerfil(guardado.getImagenPerfil());
        return dto;
    }


}
