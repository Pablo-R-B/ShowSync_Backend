package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.repositorios.PromotoresRepositorio;
import com.example.showsyncbackend.repositorios.SalasRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class SalasServicio {

    @Autowired
    private final SalasRepositorio salaRepositorio;

    @Autowired
    private final PromotoresServicio promotoresServicio;

    @Autowired
    private final EventosRepositorio eventosRepositorio;



    //solicitar sala para un evento
    public void solicitarSala(Integer salaId, Integer promotorId, String nombreEvento, String descripcion, LocalDate fecha) {
        // Obtener la sala por su ID
        Salas sala = salaRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        // Obtener el promotor por su ID
        Promotores promotor = promotoresServicio.obtenerPromotorPorId(promotorId);

        // Crear un nuevo evento en estado "en_revision"
        Eventos nuevoEvento = Eventos.builder()
                .sala_id(sala)
                .promotor(promotor)
                .nombre_evento(nombreEvento)
                .descripcion(descripcion)
                .fecha_evento(fecha)
                .estado(Estado.en_revision)
                .imagen_evento(null) // Puedes poner una imagen por defecto si quieres
                .build();

        eventosRepositorio.save(nuevoEvento);

        System.out.println("El promotor " + promotor.getNombrePromotor() + " ha solicitado la sala '" + sala.getNombre() + "' para el evento: " + nombreEvento);
    }





}
