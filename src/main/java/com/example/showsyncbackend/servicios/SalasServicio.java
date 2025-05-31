package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.CrearSalaRequestDTO;
import com.example.showsyncbackend.dtos.DisponibilidadSalaDTO;
import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.dtos.SalaEstadoCantidadDTO;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final CloudinaryService cloudinaryService;


    public SalaDTO crearSala(CrearSalaRequestDTO request, MultipartFile imagenArchivo) {
        Claims claims = obtenerClaimsDesdeContexto();
        String email = claims.getSubject();

        Usuario administrador = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String imagenUrl = null;
        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            try {
                imagenUrl = cloudinaryService.uploadFile(imagenArchivo);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen a Cloudinary", e);
            }
        }

        Salas nuevaSala = Salas.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .capacidad(request.getCapacidad())
                .descripcion(request.getDescripcion())
                .imagen(imagenUrl) // usar URL de Cloudinary
                .ciudad(request.getCiudad())
                .provincia(request.getProvincia())
                .codigoPostal(request.getCodigoPostal())
                .administrador(administrador)
                .build();

        if (salasRepositorio.existsByNombreAndDireccion(request.getNombre(), request.getDireccion())) {
            throw new RuntimeException("Ya existe una sala con el mismo nombre y dirección");
        }

        if (request.getCapacidad() <= 0) {
            throw new RuntimeException("La capacidad debe ser mayor a 0");
        }

        if (request.getCiudad() == null || request.getCiudad().isEmpty()) {
            throw new RuntimeException("La ciudad no puede estar vacía");
        }

        if (request.getProvincia() == null || request.getProvincia().isEmpty()) {
            throw new RuntimeException("La provincia no puede estar vacía");
        }

        if (request.getCodigoPostal() == null || request.getCodigoPostal().isEmpty()) {
            throw new RuntimeException("El código postal no puede estar vacío");
        }

        if (request.getDescripcion() == null || request.getDescripcion().isEmpty() || request.getDescripcion().length() < 20 || request.getDescripcion().length() > 500) {
            throw new RuntimeException("La descripción no puede estar vacía");
        }

        if (request.getNombre() == null || request.getNombre().isEmpty() || request.getNombre().length() < 3 || request.getNombre().length() > 50) {
            throw new RuntimeException("El nombre de la sala debe tener entre 3 y 50 caracteres");
        }

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

    public SalaDTO editarSala(Integer salaId, CrearSalaRequestDTO request, MultipartFile imagenArchivo) {
        Salas sala = salasRepositorio.findById(salaId)
                .orElseThrow(() -> new RuntimeException("Sala no encontrada"));

        sala.setNombre(request.getNombre());
        sala.setDireccion(request.getDireccion());
        sala.setCapacidad(request.getCapacidad());
        sala.setDescripcion(request.getDescripcion());
        sala.setCiudad(request.getCiudad());
        sala.setProvincia(request.getProvincia());
        sala.setCodigoPostal(request.getCodigoPostal());

        if (imagenArchivo != null && !imagenArchivo.isEmpty()) {
            try {
                String imagenUrl = cloudinaryService.uploadFile(imagenArchivo);
                sala.setImagen(imagenUrl);
            } catch (IOException e) {
                throw new RuntimeException("Error al subir imagen a Cloudinary", e);
            }
        }

        if (salasRepositorio.existsByNombreAndDireccion(request.getNombre(), request.getDireccion())) {
            throw new RuntimeException("Ya existe una sala con el mismo nombre y dirección");
        }

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

    public List<Object[]> obtenerCantidadReservasPorSala() {
        return eventosRepositorio.obtenerCantidadReservasPorSala();
    }
    public List<SalaEstadoCantidadDTO> obtenerCantidadReservasPorSalaYEstadoDTO() {
        return eventosRepositorio.obtenerCantidadReservasPorSalaYEstado()
                .stream()
                .map(obj -> new SalaEstadoCantidadDTO(
                        (String) obj[0],
                        ((Estado) obj[1]).name(),
                        (Long) obj[2]
                ))
                .collect(Collectors.toList());
    }

}