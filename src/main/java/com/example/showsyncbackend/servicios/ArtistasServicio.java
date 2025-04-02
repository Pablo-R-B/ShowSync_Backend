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

//    private ArtistasCatalogoDTO convertirAArtistasCatalogoDTO(Artistas artista) {
//        ArtistasCatalogoDTO dto = new ArtistasCatalogoDTO();
//        dto.setNombre(artista.getNombreArtista());
//        dto.setImagen(artista.getImagenPerfil());
//        List<String> generos = artista.getGenerosMusicales()
//                .stream()
//                .map(GenerosMusicales::getNombre)
//                .collect(Collectors.toList());
//        System.out.println("Artista: " + artista.getNombreArtista() + ", Géneros: " + generos);
//        dto.setGeneros(generos);
//        return dto;
//
//    }





}
