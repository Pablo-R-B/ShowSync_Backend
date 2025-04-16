package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.SalasRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SalasServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final SalasRepositorio salasRepositorio;

    public Salas crearSala(CrearSalaRequestDTO request) {
        // Obtener el email del usuario autenticado
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario administrador = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));


        Salas nuevaSala = Salas.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .capacidad(request.getCapacidad())
                .descripcion(request.getDescripcion())
                .imagen(request.getImagen())
                .ciudad(request.getCiudad())
                .provincia(request.getProvincia())
                .codigoPostal(request.getCodigoPostal())
                .administrador(administrador)
                .build();


        return salasRepositorio.save(nuevaSala);
    }

    public Salas editarSala(Integer salaId, CrearSalaRequestDTO request) {
        Salas salaExistente = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        salaExistente.setNombre(request.getNombre());
        salaExistente.setDireccion(request.getDireccion());
        salaExistente.setCapacidad(request.getCapacidad());
        salaExistente.setDescripcion(request.getDescripcion());
        salaExistente.setImagen(request.getImagen());

        return salasRepositorio.save(salaExistente);
    }

    public void eliminarSala(Integer salaId) {
        if (!salasRepositorio.existsById(salaId)) {
            throw new RuntimeException("Sala no encontrada");
        }
        salasRepositorio.deleteById(salaId);
    }

    public Salas obtenerSalaPorId(Integer id) {
        return salasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
    }

    public List<Salas> obtenerTodasLasSalas() {
        return salasRepositorio.findAll();
    }

    public List<Salas> buscarSalas(String filtro) {
        return salasRepositorio.buscarSalas(filtro);
    }





}

