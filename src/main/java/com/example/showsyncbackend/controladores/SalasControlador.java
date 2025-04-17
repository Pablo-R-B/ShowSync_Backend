package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.servicios.SalasServicio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/sala")
@AllArgsConstructor
public class SalasControlador {

    @Autowired
    private SalasServicio salasServicio;

    /**
     * Solicitar sala para un evento
     *
     * @param salaId       ID de la sala
     * @param promotorId   ID del promotor
     * @param nombreEvento  Nombre del evento
     * @param descripcion  Descripción del evento
     * @param fecha        Fecha del evento
     * @return Mensaje de éxito
     */

    @PostMapping("/solicitar")
    public String solicitarSala(
            @RequestParam Integer salaId,
            @RequestParam Integer promotorId,
            @RequestParam String nombreEvento,
            @RequestParam String descripcion,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        salasServicio.solicitarSala(salaId, promotorId, nombreEvento, descripcion, fecha);
        return "Solicitud de sala enviada correctamente.";
    }
}
