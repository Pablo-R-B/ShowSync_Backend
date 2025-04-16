package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.modelos.DisponibilidadSalas;
import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.DisponibilidadSalasRepositorio;
import com.example.showsyncbackend.repositorios.SalasRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SalasServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final SalasRepositorio salasRepositorio;
    private final DisponibilidadSalasRepositorio disponibilidadSalasRepositorio;

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

        // Guardar la sala en la base de datos
        Salas salaGuardada = salasRepositorio.save(nuevaSala);

        // Registrar disponibilidad para los próximos 30 días
        LocalDate fechaInicio = LocalDate.now();
        for (int i = 0; i < 90; i++) {
            DisponibilidadSalas disponibilidad = new DisponibilidadSalas();
            disponibilidad.setSala(salaGuardada);
            disponibilidad.setFecha(fechaInicio.plusDays(i));
            disponibilidad.setDisponibilidad(true);
            disponibilidadSalasRepositorio.save(disponibilidad);
        }

        return salaGuardada;
    }






    public Salas editarSala(Integer salaId, CrearSalaRequestDTO request) {
        Salas salaExistente = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        salaExistente.setNombre(request.getNombre());
        salaExistente.setDireccion(request.getDireccion());
        salaExistente.setCapacidad(request.getCapacidad());
        salaExistente.setDescripcion(request.getDescripcion());
        salaExistente.setImagen(request.getImagen());
        salaExistente.setProvincia(request.getProvincia());
        salaExistente.setCiudad(request.getCiudad());
        salaExistente.setCodigoPostal(request.getCodigoPostal());


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


    public List<Salas> filtrarPorCapacidad(Integer min, Integer max) {
        return salasRepositorio.filtrarPorCapacidad(min, max);
    }

    private SalaDTO convertirASalaDTO(Salas sala) {
        SalaDTO salaDTO = new SalaDTO();
        salaDTO.setId(sala.getId());
        salaDTO.setNombre(sala.getNombre());
        salaDTO.setDireccion(sala.getDireccion());
        salaDTO.setCapacidad(sala.getCapacidad());
        salaDTO.setImagen(sala.getImagen());
        salaDTO.setDescripcion(sala.getDescripcion());
        return salaDTO;
    }

    private DisponibilidadSalaDTO convertirADTO(DisponibilidadSalas disponibilidad) {
        DisponibilidadSalaDTO dto = new DisponibilidadSalaDTO();
        dto.setId(disponibilidad.getId());
        dto.setFecha(disponibilidad.getFecha());
        dto.setDisponibilidad(disponibilidad.getDisponibilidad());
        dto.setSala(convertirASalaDTO(disponibilidad.getSala())); // <-- aquí el cambio clave
        return dto;
    }



    public List<DisponibilidadSalaDTO> consultarDisponibilidad(Integer salaId, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate hoy = LocalDate.now();
        LocalDate maxFecha = hoy.plusDays(90);

        if (fechaInicio.isBefore(hoy) || fechaFin.isAfter(maxFecha)) {
            throw new IllegalArgumentException("La consulta solo puede realizarse entre hoy y los próximos 30 días.");
        }

        List<DisponibilidadSalas> disponibilidadesEntidad =
                disponibilidadSalasRepositorio.findByFechaBetweenAndSalaId(fechaInicio, fechaFin, salaId);

        if (disponibilidadesEntidad.isEmpty()) {
            List<DisponibilidadSalas> disponibilidadGenerada = new ArrayList<>();
            LocalDate currentDate = fechaInicio;

            Salas sala = salasRepositorio.findById(salaId)
                    .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

            while (!currentDate.isAfter(fechaFin)) {
                DisponibilidadSalas disponibilidad = new DisponibilidadSalas();
                disponibilidad.setSala(sala);
                disponibilidad.setFecha(currentDate);
                disponibilidad.setDisponibilidad(true);
                disponibilidadGenerada.add(disponibilidad);
                currentDate = currentDate.plusDays(1);
            }

            return disponibilidadGenerada.stream()
                    .map(this::convertirADTO)
                    .toList();
        }

        return disponibilidadesEntidad.stream()
                .map(this::convertirADTO)
                .toList();

    }















}

