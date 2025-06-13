package com.example.showsyncbackend.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant; // ¡ESTO DEBE SER INSTANT!
// Asegúrate de que NO tengas 'import java.time.LocalDateTime;' aquí

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajeChatDTO {
    private Long id; // Este ID es el de la base de datos
    private String contenido;
    private String remitente;
    private String tipo;
    private String imagenRemitenteUrl;

    private Instant fechaEnvio;

    private String temporalId;
}
