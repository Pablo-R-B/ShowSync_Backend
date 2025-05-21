package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.RespuestaEventoRevisionDTO;
import com.example.showsyncbackend.dtos.EventosDTO;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.servicios.EventosServicio;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/eventos")
public class EventosControlador {

    @Autowired
    private EventosServicio eventosServicio;


    /**
     * Obtener eventos confirmados
     * @return
     */
    @GetMapping("/confirmados")
    public ResponseEntity<List<EventosDTO>> obtenerEventosConfirmados() {
        List<EventosDTO> eventos = eventosServicio.obtenerEventosConfirmados();
        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    /**
     * Obtener catálogo de eventos
     * @return
     */
    @GetMapping("/catalogo")
    public ResponseEntity<List<EventosDTO>> obtenerCatalogoEventos() {
        List<EventosDTO> eventos = eventosServicio.obtenerCatalogoEventos();
        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }


    /**
     * Obtener eventos de un promotor
     * @param promotorId
     * @return
     */
    @GetMapping("/promotor/{promotorId}")
    public ResponseEntity<List<EventosDTO>> obtenerEventosDePromotor(@PathVariable Integer promotorId) {
        List<EventosDTO> eventos = eventosServicio.obtenerEventosDePromotor(promotorId);
        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }

    /**
     * Obtener eventos de un promotor a partir de un id de usuario   */
    @GetMapping("promotor/usuarioPromotor/{usuarioIdPromotor}")
    public ResponseEntity<List<EventosDTO>> obtenerEventosDePromotorPorUsuario(@PathVariable Integer usuarioIdPromotor) {
        List<EventosDTO> eventosDTOS = eventosServicio.listarEventosPorIdUsuario(usuarioIdPromotor);
        return new ResponseEntity<>(eventosDTOS, HttpStatus.OK);
    }

    /**
     * Obtener eventos por estado
     * @param eventoNuevo
     * @return
     */
    @PostMapping("/promotor/{promotorId}")
    public ResponseEntity<Eventos> crearEvento(@PathVariable Integer promotorId, @RequestBody Eventos eventoNuevo) {
        Eventos evento = eventosServicio.crearEvento(promotorId, eventoNuevo);
        return new ResponseEntity<>(evento, HttpStatus.CREATED);
    }

    /**
     * Editar evento de un promotor
     * @param promotorId ID del promotor
     * @param eventoId ID del evento
     * @param eventoActualizado Datos nuevos del evento
     * @return Evento actualizado como DTO
     */
    @PutMapping("/promotor/{promotorId}/evento/{eventoId}")
    public ResponseEntity<EventosDTO> editarEvento(@PathVariable Integer promotorId,
                                                  @PathVariable Integer eventoId,
                                                  @RequestBody Eventos eventoActualizado) {
        eventoActualizado.setId(eventoId);
        EventosDTO eventoDTO = eventosServicio.editarEvento(promotorId, eventoActualizado);
        return new ResponseEntity<>(eventoDTO, HttpStatus.OK);
    }


    /**
     * Eliminar evento
     * @param promotorId
     * @param eventoId
     * @return
     */
    @DeleteMapping("/promotor/{promotorId}/evento/{eventoId}")
    public ResponseEntity<String> eliminarEvento(@PathVariable Integer promotorId,
                                                 @PathVariable Integer eventoId) {
        eventosServicio.eliminarEvento(promotorId, eventoId);
        return new ResponseEntity<>("Evento eliminado correctamente", HttpStatus.OK);
    }


    /**
     * Solicitar sala para un evento
     * @return
     */
    @GetMapping("/todos")
    public ResponseEntity<List<EventosDTO>> listarTodosLosEventos() {
        List<EventosDTO> eventos = eventosServicio.listarTodosLosEventos();
        return new ResponseEntity<>(eventos, HttpStatus.OK);
    }


    /**
     * Obtener perfil de evento por ID
     * @param eventoId
     * @return
     */
    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<EventosDTO> mostrarPerfilEvento(@PathVariable Integer eventoId) {
        EventosDTO evento = eventosServicio.obtenerEventoPorId(eventoId);
        return new ResponseEntity<>(evento, HttpStatus.OK);
    }

    /**
     * Actualizar seguimiento de un evento
     * @param id ID del evento
     * @param body Contenido del cuerpo de la solicitud
     * @return Respuesta sin contenido
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Void> actualizarSeguimiento(@PathVariable Integer id,
                                                      @RequestBody Map<String, Boolean> body) {
        // Verificar si el cuerpo de la solicitud contiene el campo "seguido"
        boolean seguido = body.getOrDefault("seguido", false);

        // Llamar al servicio para actualizar el evento
        try {
            eventosServicio.actualizarSeguimiento(id, seguido);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Si no se encuentra el evento, devolver un 404 con el mensaje de error
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }


    /**
     * Obtener lista de géneros desde la base de datos
     * @return Lista de géneros
     */
    @GetMapping("/generos")
    public ResponseEntity<List<String>> obtenerGeneros() {
        List<String> generosMusicales = eventosServicio.obtenerGeneros();
        return ResponseEntity.ok(generosMusicales);
    }


    /**
     * Obtener lista de estados desde la base de datos
     * @return Lista de estados
     */
    @GetMapping("/estados")
    public ResponseEntity<List<String>> obtenerEstados() {
        List<String> estados = eventosServicio.obtenerEstados();
        return new ResponseEntity<>(estados, HttpStatus.OK);
    }

    @PostMapping("/reserva/sala")
    public ResponseEntity<?> crearEventoEnRevision(@RequestBody EventosDTO dto) {
        try {
            RespuestaEventoRevisionDTO evento = eventosServicio.crearEventoEnRevision(dto);
            return ResponseEntity.ok(evento);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiError(HttpStatus.NOT_FOUND.value(), e.getMessage()));
        } catch (Exception e) {
            // Aquí controlas cualquier otro error (como conflicto de sala)
            String mensaje = e.getMessage().contains("Evento ya reservado") ? e.getMessage() : "Error interno del servidor";
            HttpStatus status = mensaje.equals("Error interno del servidor") ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.CONFLICT;
            return ResponseEntity.status(status)
                    .body(new ApiError(status.value(), mensaje));
        }
    }


    public record ApiError(int value, String message) {}











}
