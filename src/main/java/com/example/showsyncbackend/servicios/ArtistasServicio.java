package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ArtistasServicio {

    @Autowired
    private ArtistasRepositorio artistasRepositorio;

    public RespuestaPaginacionDTO<ArtistasCatalogoDTO> obtenerArtistasConGeneros(int page, int size, String termino) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Artistas> pageEntidades;

        if (termino != null && !termino.isEmpty()) {
            pageEntidades = artistasRepositorio.findArtistasByNombre(termino, pageable);
        } else {
            pageEntidades = artistasRepositorio.findAllWithGeneros(pageable);
        }

        Page<ArtistasCatalogoDTO> pageDTO = pageEntidades.map(art ->
                new ArtistasCatalogoDTO(
                        art.getId(),
                        art.getNombreArtista(),
                        art.getImagenPerfil(),
                        art.getGenerosMusicales()
                                .stream()
                                .map(GenerosMusicales::getNombre)
                                .collect(Collectors.toList())
                )
        );

        RespuestaPaginacionDTO<ArtistasCatalogoDTO> respuesta = new RespuestaPaginacionDTO<>();
        respuesta.setItems(pageDTO.getContent());
        respuesta.setTotalItems((int) pageDTO.getTotalElements());
        respuesta.setTotalPages(pageDTO.getTotalPages());
        respuesta.setCurrentPage(page);
        respuesta.setPageSize(size);
        return respuesta;
    }


    public RespuestaPaginacionDTO<ArtistasCatalogoDTO> artistasPorGenero(
            String genero,
            String termino,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Artistas> pageEntidades = artistasRepositorio.findArtistasByGenero(
                genero,
                termino,
                pageable
        );

        Page<ArtistasCatalogoDTO> pageDTO = pageEntidades.map(art ->
                new ArtistasCatalogoDTO(
                        art.getId(),
                        art.getNombreArtista(),
                        art.getImagenPerfil(),
                        art.getGenerosMusicales()
                                .stream()
                                .map(GenerosMusicales::getNombre)
                                .collect(Collectors.toList())
                )
        );

        RespuestaPaginacionDTO<ArtistasCatalogoDTO> respuesta = new RespuestaPaginacionDTO<>();
        respuesta.setItems(pageDTO.getContent());
        respuesta.setTotalItems((int) pageDTO.getTotalElements());
        respuesta.setTotalPages(pageDTO.getTotalPages());
        respuesta.setCurrentPage(page);
        respuesta.setPageSize(size);
        return respuesta;
    }

    public ArtistasCatalogoDTO artistaPorId(Integer id) {
        // JPQL fetch-join:
        return artistasRepositorio.findByIdWithGeneros(id)
                .map(art -> mapToDto(art.getId(),
                        art.getNombreArtista(),
                        art.getImagenPerfil(),
                        art.getGenerosMusicales()
                                .stream()
                                .map(GenerosMusicales::getNombre)
                                .collect(Collectors.toList())))
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado")
                );
    }
        private ArtistasCatalogoDTO mapToDto(Integer id,
                String nombre,
                String imagen,
                List<String> generos) {
            return new ArtistasCatalogoDTO(id, nombre, imagen, generos);
        }

}
