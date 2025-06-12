package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MensajeDTO {
    private String contenido;
    private String remitente;
    private String tipo;
    private String imagenRemitenteUrl;
}
