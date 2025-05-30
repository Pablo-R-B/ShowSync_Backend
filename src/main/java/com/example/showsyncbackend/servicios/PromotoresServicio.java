package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.EvetosPostulacionArtistaDTO;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PromotoresServicio {

    private final EventosRepositorio eventosRepository;
    private final EventosRepositorio eventosRepositorio;
    private final PromotoresRepositorio promotoresRepositorio;

    //  Obtener perfil del promotor
    public Promotores obtenerPromotorPorId(Integer id) {
        return promotoresRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
    }

    //  Listar todos los promotores
    public List<Promotores> listarPromotores() {
        return promotoresRepositorio.findAll();
    }

    //  Crear un nuevo promotor
    public Promotores crearPromotor(Promotores promotor) {
        return promotoresRepositorio.save(promotor);
    }

    // Editar un promotor
    public Promotores editarPromotor(Integer id, Promotores promotor) {
        Promotores promotorExistente = obtenerPromotorPorId(id);
        promotorExistente.setUsuario(promotor.getUsuario());
        promotorExistente.setNombrePromotor(promotor.getNombrePromotor());
        promotorExistente.setDescripcion(promotor.getDescripcion());
        return promotoresRepositorio.save(promotorExistente);
    }

    // Eliminar un promotor
    public void eliminarPromotor(Integer id) {
        Promotores promotor = obtenerPromotorPorId(id);
        List<Eventos> eventos = eventosRepository.findByPromotor(promotor);
        if (!eventos.isEmpty()) {
            throw new RuntimeException("No se puede eliminar el promotor porque tiene eventos asociados");
        }
        promotoresRepositorio.delete(promotor);
    }


//     Obtener promotor por idUsuario
    public Promotores obtenerPromotorPorUsuarioId(Integer usuarioId) {
        return promotoresRepositorio.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
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

}
