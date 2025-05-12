package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class PromotoresServicio {

    private final PromotoresRepositorio promotoresRepository;
    private final EventosRepositorio eventosRepository;
    private final ArtistasRepositorio artistasRepositorio;

    //  Obtener perfil del promotor
    public Promotores obtenerPromotorPorId(Integer id) {
        return promotoresRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
    }

    //  Listar todos los promotores
    public List<Promotores> listarPromotores() {
        return promotoresRepository.findAll();
    }

    //  Crear un nuevo promotor
    public Promotores crearPromotor(Promotores promotor) {
        return promotoresRepository.save(promotor);
    }

    // Editar un promotor
    public Promotores editarPromotor(Integer id, Promotores promotor) {
        Promotores promotorExistente = obtenerPromotorPorId(id);
        promotorExistente.setUsuario(promotor.getUsuario());
        promotorExistente.setNombrePromotor(promotor.getNombrePromotor());
        promotorExistente.setDescripcion(promotor.getDescripcion());
        return promotoresRepository.save(promotorExistente);
    }

    // Eliminar un promotor
    public void eliminarPromotor(Integer id) {
        Promotores promotor = obtenerPromotorPorId(id);
        List<Eventos> eventos = eventosRepository.findByPromotor(promotor);
        if (!eventos.isEmpty()) {
            throw new RuntimeException("No se puede eliminar el promotor porque tiene eventos asociados");
        }
        promotoresRepository.delete(promotor);
    }


}
