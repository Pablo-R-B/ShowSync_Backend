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
        // Obtener usuario administrador autenticado
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

        // Inicializar disponibilidad para los próximos 90 días (no solo 7)
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

    // En SalasServicio.java

    public DisponibilidadSalaDTO consultarDisponibilidadPorFecha(Integer salaId, LocalDate fecha) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        List<Estado> estados = List.of(Estado.en_revision, Estado.confirmado);

        List<Eventos> eventosEnFecha = eventosRepositorio.findBySalaAndEstadoInAndFechaEventoBetween(
                sala,
                estados,
                fecha,
                fecha
        );

        DisponibilidadSalaDTO dto = new DisponibilidadSalaDTO();
        dto.setFecha(fecha);
        dto.setSalaId(salaId);

        if (eventosEnFecha.isEmpty()) {
            dto.setDisponibilidad(true);
            dto.setEventoId(null);
            dto.setEstadoEvento(null);
        } else {
            Eventos evento = eventosEnFecha.get(0); // asumimos uno por fecha
            dto.setDisponibilidad(false);
            dto.setEventoId(evento.getId());
            dto.setEstadoEvento(evento.getEstado().name()); // 👈 esto es lo nuevo
        }


        return dto;
    }

    public List<DisponibilidadSalaDTO> consultarFechasNoDisponibles(Integer salaId) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        List<Estado> estados = List.of(Estado.en_revision, Estado.confirmado);

        // Trae todos los eventos con estado relevante para esta sala
        List<Eventos> eventos = eventosRepositorio.findBySalaAndEstadoIn(sala, estados);

        // Convertimos cada evento a un DTO de disponibilidad
        return eventos.stream().map(evento -> {
            DisponibilidadSalaDTO dto = new DisponibilidadSalaDTO();
            dto.setFecha(evento.getFechaEvento());
            dto.setSalaId(salaId);
            dto.setDisponibilidad(false); // Porque estamos listando los no disponibles
            dto.setEstadoEvento(evento.getEstado().name());
            dto.setEventoId(evento.getId());
            return dto;
        }).toList();
    }



    public void solicitarSala(Integer salaId, Integer promotorId, String nombreEvento, String descripcion, LocalDate fecha) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));
        Promotores promotor = promotoresServicio.obtenerPromotorPorId(promotorId);

        Eventos evento = Eventos.builder()
                .sala(sala)
                .promotor(promotor)
                .nombre_evento(nombreEvento)
                .descripcion(descripcion)
                .fechaEvento(fecha)
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

    // Buscar sala por ciudad
    public List<SalaDTO> buscarSalasPorCiudad(String ciudad) {
        return salasRepositorio.findByCiudad(ciudad).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    // Buscar sala por provincia
    public List<SalaDTO> buscarSalasPorProvincia(String provincia) {
        return salasRepositorio.findByProvincia(provincia).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }
}
