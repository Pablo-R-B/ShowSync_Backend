package com.example.showsyncbackend.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArtistasCatalogoDTO {
    Integer id;
    String nombreArtista;
    String imagenPerfil;
    String biografia;
    List<String> generosMusicales;

}
