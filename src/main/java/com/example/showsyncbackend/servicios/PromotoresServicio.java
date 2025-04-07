package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.repositorios.EventosRepository;
import com.example.showsyncbackend.repositorios.PromotoresRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PromotoresServicio {

    @Autowired
    private PromotoresRepository promotoresRepository;
    private EventosRepository eventosRepository;

    public PromotoresServicio(PromotoresRepository promotorRepositorio) {
        this.promotoresRepository = promotorRepositorio;
    }

    public Promotores obtenerPromotorPorId(Integer id) {
        return promotoresRepository.findById(id).orElseThrow(() -> new RuntimeException("Promotor no encontrado"));
    }

    public List<Promotores> listarPromotores() {
        return promotoresRepository.findAll();
    }

    public List<Eventos> listarEventosDePromotor(Integer id) {
        return eventosRepository.findByPromotorId(id);
    }

    public void solicitarSala(Integer id, String sala) {
        Promotores promotor = obtenerPromotorPorId(id);
        System.out.println("El promotor " + promotor.getNombrePromotor() + " ha solicitado la sala: " + sala);
    }
}
