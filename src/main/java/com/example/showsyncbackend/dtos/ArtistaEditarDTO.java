package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaEditarDTO {
    Integer id;
    String nombreArtista;
    String imagenPerfil;
    String biografia;
    String musicUrl;
    List<GenerosMusicalesDTO> generosMusicales;
}
