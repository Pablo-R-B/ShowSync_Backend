package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ArtistasServicio {

    @Autowired
    private ArtistasRepositorio artistasRepositorio;

    public List<Artistas> findAll() {
        return artistasRepositorio.findAll();
    }


    public List<Map<String, Object>> obtenerArtistasConGeneros() {
        List<Object[]> resultados = artistasRepositorio.findAllWithGeneros();
        Map<String, Map<String, Object>> artistasMap = new HashMap<>();

        for (Object[] fila : resultados) {
            String id = String.valueOf(fila[0]);
            String nombreArtista = (String) fila[1];
            String imagen = (String) fila[2];
            String nombreGenero = (String) fila[3];

            // Si el artista ya está en el mapa, agregamos el nuevo género a su lista
            if (!artistasMap.containsKey(id)) {
                Map<String, Object> artista = new HashMap<>();
                artista.put("id", id);
                artista.put("nombreArtista", nombreArtista);
                artista.put("nombreGenero", new ArrayList<String>());
                artista.put("imagen", imagen);
                artistasMap.put(id, artista);
            }

            // Añadir el género a la lista de géneros del artista
            ((List<String>) artistasMap.get(id).get("nombreGenero")).add(nombreGenero);
        }

        return new ArrayList<>(artistasMap.values());
    }

    public List<ArtistasCatalogoDTO> artistasPorGenero(String genero) {

        List<Artistas> artistas = artistasRepositorio.findArtistasByGenero(genero);
        return artistas.stream()
                .map(this::convertirAArtistasCatalogoDTO)
                .collect(Collectors.toList());
    }

    private ArtistasCatalogoDTO convertirAArtistasCatalogoDTO(Artistas artista) {
        ArtistasCatalogoDTO dto = new ArtistasCatalogoDTO();
        dto.setNombre(artista.getNombreArtista());
        dto.setImagen(artista.getImagenPerfil());

        List<String> generos = artista.getGenerosMusicales()
                .stream()
                .map(GenerosMusicales::getNombre)
                .collect(Collectors.toList());
        dto.setGeneros(generos);
        return dto;

    }





}
