package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaDTO {
    private String nombreArtista;
    private String biografia;
    private String imagenPerfil;
    private String musicUrl;
}
