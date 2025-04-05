package com.example.showsyncbackend.dtos;

import com.example.showsyncbackend.modelos.GenerosMusicales;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
//@AllArgsConstructor
public class ArtistasCatalogoDTO {
    String nombre;
    String imagen;
    List<String> generos;
}
