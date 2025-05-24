package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.*;
import com.example.showsyncbackend.repositorios.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalasServicio {

    private final UsuarioRepositorio usuarioRepositorio;
    private final DisponibilidadSalasRepositorio disponibilidadSalasRepositorio;
    private final SalasRepositorio salasRepositorio;
    private final PromotoresServicio promotoresServicio;
    private final EventosRepositorio eventosRepositorio;

    public SalaDTO crearSala(CrearSalaRequestDTO request) {
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

        Salas salaGuardada = salasRepositorio.save(nuevaSala);

        LocalDate fechaInicio = LocalDate.now();
        for (int i = 0; i < 90; i++) {
            disponibilidadSalasRepositorio.save(
                    DisponibilidadSalas.builder()
                            .sala(salaGuardada)
                            .fecha(fechaInicio.plusDays(i))
                            .disponibilidad(true)
                            .build()
            );
        }

        return convertirASalaDTO(salaGuardada);
    }

    public SalaDTO editarSala(Integer salaId, CrearSalaRequestDTO request) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        sala.setNombre(request.getNombre());
        sala.setDireccion(request.getDireccion());
        sala.setCapacidad(request.getCapacidad());
        sala.setDescripcion(request.getDescripcion());
        sala.setImagen(request.getImagen());
        sala.setCiudad(request.getCiudad());
        sala.setProvincia(request.getProvincia());
        sala.setCodigoPostal(request.getCodigoPostal());

        return convertirASalaDTO(salasRepositorio.save(sala));
    }

    public void eliminarSala(Integer salaId) {
        if (!salasRepositorio.existsById(salaId)) {
            throw new RuntimeException("Sala no encontrada");
        }
        salasRepositorio.deleteById(salaId);
    }

    public SalaDTO obtenerSalaPorId(Integer id) {
        Salas sala = salasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
        return convertirASalaDTO(sala);
    }

    public List<SalaDTO> obtenerTodasLasSalas() {
        return salasRepositorio.findAll().stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    public List<SalaDTO> buscarSalas(String filtro) {
        return salasRepositorio.buscarSalas(filtro).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    public List<SalaDTO> filtrarPorCapacidad(Integer min, Integer max) {
        return salasRepositorio.filtrarPorCapacidad(min, max).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    public List<DisponibilidadSalaDTO> consultarDisponibilidad(Integer salaId, LocalDate fechaInicio, LocalDate fechaFin) {
        LocalDate hoy = LocalDate.now();

        // Si no se proporciona fechaFin, se establece un valor predeterminado (por ejemplo, 1 año en el futuro)
        if (fechaFin == null) {
            fechaFin = fechaInicio.plusYears(1);
        }

        if (fechaInicio.isBefore(hoy)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser anterior a hoy.");
        }

        List<DisponibilidadSalas> disponibilidades =
                disponibilidadSalasRepositorio.findByFechaBetweenAndSalaId(fechaInicio, fechaFin, salaId);

        if (disponibilidades.isEmpty()) {
            List<DisponibilidadSalas> generadas = new ArrayList<>();
            Salas sala = salasRepositorio.findById(salaId)
                    .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
            LocalDate fecha = fechaInicio;
            while (!fecha.isAfter(fechaFin)) {
                generadas.add(DisponibilidadSalas.builder()
                        .sala(sala)
                        .fecha(fecha)
                        .disponibilidad(true)
                        .build());
                fecha = fecha.plusDays(1);
            }
            return generadas.stream().map(this::convertirADTO).collect(Collectors.toList());
        }

        return disponibilidades.stream().map(this::convertirADTO).collect(Collectors.toList());
    }

    public void solicitarSala(Integer salaId, Integer promotorId, String nombreEvento, String descripcion, LocalDate fecha) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
        Promotores promotor = promotoresServicio.obtenerPromotorPorId(promotorId);

        Eventos evento = Eventos.builder()
                .sala_id(sala)
                .promotor(promotor)
                .nombre_evento(nombreEvento)
                .descripcion(descripcion)
                .fecha_evento(fecha)
                .estado(Estado.en_revision)
                .imagen_evento(null)
                .build();

        eventosRepositorio.save(evento);
    }

    private SalaDTO convertirASalaDTO(Salas sala) {
        SalaDTO dto = new SalaDTO();
        dto.setId(sala.getId());
        dto.setNombre(sala.getNombre());
        dto.setDireccion(sala.getDireccion());
        dto.setCapacidad(sala.getCapacidad());
        dto.setImagen(sala.getImagen());
        dto.setDescripcion(sala.getDescripcion());
        dto.setCiudad(sala.getCiudad());
        dto.setProvincia(sala.getProvincia());
        dto.setCodigoPostal(sala.getCodigoPostal());
        return dto;
    }

    private DisponibilidadSalaDTO convertirADTO(DisponibilidadSalas d) {
        DisponibilidadSalaDTO dto = new DisponibilidadSalaDTO();
        dto.setFecha(d.getFecha());
        dto.setDisponibilidad(d.getDisponibilidad());
        dto.setSalaId(d.getSala().getId());
        return dto;
    }

    //Buscar sala por ciudad
    public List<SalaDTO> buscarSalasPorCiudad(String ciudad) {
        return salasRepositorio.findByCiudad(ciudad).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    //Buscar sala por provincia
    public List<SalaDTO> buscarSalasPorProvincia(String provincia) {
        return salasRepositorio.findByProvincia(provincia).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }
}
