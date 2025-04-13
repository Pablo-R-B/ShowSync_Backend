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
            String imagenPerfil = (String) fila[2];
            String generoMusical = (String) fila[3];

            // Si el artista ya está en el mapa, agregamos el nuevo género a su lista
            if (!artistasMap.containsKey(id)) {
                Map<String, Object> artista = new HashMap<>();
                artista.put("id", id);
                artista.put("nombre", nombreArtista);
                artista.put("generos", new ArrayList<String>());
                artista.put("imagen", imagenPerfil);
                artistasMap.put(id, artista);
            }

            // Añadir el género a la lista de géneros del artista
            ((List<String>) artistasMap.get(id).get("generos")).add(generoMusical);
        }

        return new ArrayList<>(artistasMap.values());
    }

    public List<ArtistasCatalogoDTO> artistasPorGenero(String genero) {
        List<Object[]> resultados = artistasRepositorio.findArtistasByGenero(genero);
        Map<String, ArtistasCatalogoDTO> artistasMap = new HashMap<>();

        for (Object[] resultado : resultados) {
            Integer id = (Integer) resultado[0];
            String nombreArtista = (String) resultado[1];
            String imagenPerfil = (String) resultado[2];
            String generoMusical = (String) resultado[3];
            ArtistasCatalogoDTO dto;

            if (artistasMap.containsKey(nombreArtista)) {
                dto = artistasMap.get(nombreArtista);

            } else {
                dto = new ArtistasCatalogoDTO();
                dto.setNombre(nombreArtista);
                dto.setImagen(imagenPerfil);
                dto.setGeneros(new ArrayList<>());
                artistasMap.put(nombreArtista, dto);
            }
            dto.getGeneros().add(generoMusical);
        }
        return new ArrayList<>(artistasMap.values());
    }

    public List<ArtistasCatalogoDTO> buscarArtistasPorNombre(String termino) {
        // Obtener la lista de resultados de la consulta
        List<Object[]> resultados = artistasRepositorio.findArtistasByNombre(termino);
        Map<String, ArtistasCatalogoDTO> artistaMap = new HashMap<>();

        for (Object[] resultado : resultados) {
            String nombreArtista = (String) resultado[1]; // Nombre del artista
            String imagenPerfil = (String) resultado[2]; // Imagen de perfil
            String nombreGenero = (String) resultado[3]; // Nombre del género

            // Si el artista ya está en el mapa, solo agregamos el género
            if (artistaMap.containsKey(nombreArtista)) {
                ArtistasCatalogoDTO dto = artistaMap.get(nombreArtista);
                dto.getGeneros().add(nombreGenero); // Agregar el género a la lista existente
            } else {
                // Si no está, creamos un nuevo DTO
                List<String> generos = new ArrayList<>();
                generos.add(nombreGenero); // Agregar el género
                ArtistasCatalogoDTO dto = new ArtistasCatalogoDTO();
                dto.setNombre(nombreArtista);
                dto.setImagen(imagenPerfil);
                dto.setGeneros(generos);
                artistaMap.put(nombreArtista, dto);
            }

        }
        return new ArrayList<>(artistaMap.values()); // Retornar la lista de DTOs

    }


}
