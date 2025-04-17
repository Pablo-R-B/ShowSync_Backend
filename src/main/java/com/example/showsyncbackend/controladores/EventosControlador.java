package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.EventosDTO;
import com.example.showsyncbackend.modelos.Eventos;
import com.example.showsyncbackend.repositorios.EventosRepositorio;
import com.example.showsyncbackend.servicios.EventosServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/eventos")
public class EventosControlador {

    @Autowired
    private EventosServicio eventosServicio;

    @Autowired
    private EventosRepositorio eventosRepositorio;

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
    public ResponseEntity<List<Eventos>> listarTodosLosEventos() {
        List<Eventos> eventos = eventosServicio.listarTodosLosEventos();
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

}
