package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.modelos.GenerosMusicales;
import lombok.Data;

import java.util.List;

@Data
public class ArtistasCatalogoDTO {
    String nombre;
    String imagen;
    List<String> generos;
}
