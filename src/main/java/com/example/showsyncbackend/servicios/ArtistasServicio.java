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


@Service
@AllArgsConstructor
public class ArtistasServicio {

    @Autowired
    private ArtistasRepositorio artistasRepositorio;

    public List<Artistas> findAll() {
        return artistasRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> obtenerArtistasConGeneros() {
        List<Object[]> resultados = artistasRepositorio.findAllWithGeneros();
        Map<String, Map<String, Object>> artistasMap = new HashMap<>();

        for (Object[] fila : resultados) {
            String id = String.valueOf(fila[0]);
            String nombreArtista = (String) fila[1];
            String nombreGenero = (String) fila[2];

            // Si el artista ya está en el mapa, agregamos el nuevo género a su lista
            if (!artistasMap.containsKey(id)) {
                Map<String, Object> artista = new HashMap<>();
                artista.put("id", id);
                artista.put("nombreArtista", nombreArtista);
                artista.put("nombreGenero", new ArrayList<String>());
                artistasMap.put(id, artista);
            }

            // Añadir el género a la lista de géneros del artista
            ((List<String>) artistasMap.get(id).get("nombreGenero")).add(nombreGenero);
        }

        return new ArrayList<>(artistasMap.values());
    }

//    public List<ArtistasCatalogoDTO> obtenerArtistasCatalogo() {
//        try {
//            List<Object[]> resultados = artistasRepositorio.findAllWithGeneros();
//            List<ArtistasCatalogoDTO> artistasCatalogoDTOs = new ArrayList<>();
//
//            for (Object[] fila : resultados) {
//                if (fila.length < 4) { // Asegurar que el array tiene suficientes elementos
//                    System.err.println("⚠️ Fila con datos incompletos: " + Arrays.toString(fila));
//                    continue;
//                }
//
//                ArtistasCatalogoDTO dto = new ArtistasCatalogoDTO();
//                dto.setNombre((String) fila[1]); // nombre_artista
//                dto.setImagen((String) fila[2]); // imagen_perfil
//                dto.setGeneros(Collections.singletonList((String) fila[3])); // nombre del género
//
//                artistasCatalogoDTOs.add(dto);
//            }
//
//            return artistasCatalogoDTOs;
//        } catch (Exception e) {
//            System.err.println("❌ Error general al obtener artistas con géneros");
//            e.printStackTrace(); // imprime el error en consola
//            throw new RuntimeException("Error al procesar artistas", e); // relanza para que el controlador lo capture
//        }
//    }


}
