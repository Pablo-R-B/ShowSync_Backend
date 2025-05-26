package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.enumerados.Estado;
import com.example.showsyncbackend.modelos.*;
import com.example.showsyncbackend.repositorios.*;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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
        Claims claims = obtenerClaimsDesdeContexto();
        String email = claims.getSubject();

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

    // Método reutilizable para obtener Claims desde el SecurityContext
    private Claims obtenerClaimsDesdeContexto() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof Claims claims) {
            return claims;
        } else {
            throw new RuntimeException("No se pudieron obtener los claims del contexto de seguridad");
        }
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

    public List<SalaDTO> obtenerTodasLasSalas(int page, int size, String termino) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<Salas> salasPage;

        if (termino != null && !termino.trim().isEmpty()) {
            salasPage = salasRepositorio.findAllConTermino(termino.trim(), pageable);
        } else {
            salasPage = salasRepositorio.findAll(pageable);
        }

        return salasPage.getContent().stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    public List<SalaDTO> buscarSalas(String filtro) {
        return salasRepositorio.buscarSalasSinPaginacion(filtro).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    // Método con paginación para búsqueda
    public Page<SalaDTO> buscarSalasConPaginacion(String filtro, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<Salas> salasPage = salasRepositorio.buscarSalas(filtro, pageable);

        return salasPage.map(this::convertirASalaDTO);
    }

    public List<SalaDTO> filtrarPorCapacidad(Integer min, Integer max, int page, int size, String termino) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("capacidad").ascending());
        Page<Salas> salasPage;

        if (termino != null && !termino.trim().isEmpty()) {
            salasPage = salasRepositorio.filtrarPorCapacidadConTermino(min, max, termino.trim(), pageable);
        } else {
            salasPage = salasRepositorio.filtrarPorCapacidad(min, max, pageable);
        }

        return salasPage.getContent().stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

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
            Eventos evento = eventosEnFecha.get(0);
            dto.setDisponibilidad(false);
            dto.setEventoId(evento.getId());
            dto.setEstadoEvento(evento.getEstado().name());
        }

        return dto;
    }

    public List<DisponibilidadSalaDTO> consultarFechasNoDisponibles(Integer salaId) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        List<Estado> estados = List.of(Estado.en_revision, Estado.confirmado);

        List<Eventos> eventos = eventosRepositorio.findBySalaAndEstadoIn(sala, estados);

        return eventos.stream().map(evento -> {
            DisponibilidadSalaDTO dto = new DisponibilidadSalaDTO();
            dto.setFecha(evento.getFechaEvento());
            dto.setSalaId(salaId);
            dto.setDisponibilidad(false);
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

    // Buscar sala por ciudad sin paginación
    public List<SalaDTO> buscarSalasPorCiudad(String ciudad) {
        return salasRepositorio.findByCiudadIgnoreCase(ciudad).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    // Buscar sala por ciudad con paginación
    public Page<SalaDTO> buscarSalasPorCiudadConPaginacion(String ciudad, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<Salas> salasPage = salasRepositorio.findByCiudad(ciudad, pageable);

        return salasPage.map(this::convertirASalaDTO);
    }

    // Buscar sala por provincia sin paginación
    public List<SalaDTO> buscarSalasPorProvincia(String provincia) {
        return salasRepositorio.findByProvinciaIgnoreCase(provincia).stream()
                .map(this::convertirASalaDTO)
                .collect(Collectors.toList());
    }

    // Buscar sala por provincia con paginación
    public Page<SalaDTO> buscarSalasPorProvinciaConPaginacion(String provincia, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<Salas> salasPage = salasRepositorio.findByProvincia(provincia, pageable);

        return salasPage.map(this::convertirASalaDTO);
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
}