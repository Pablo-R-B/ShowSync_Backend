package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArtistasCatalogoDTO {
    Integer id;
    String nombreArtista;
    String imagenPerfil;
    String biografia;
    List<String> generosMusicales;

}
