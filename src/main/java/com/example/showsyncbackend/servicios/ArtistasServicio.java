package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.ArtistasCatalogoDTO;
import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import jakarta.persistence.EntityNotFoundException;
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
                        art.getBiografia(),
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
                        art.getBiografia(),
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
                        art.getBiografia(),
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
                String biografia,
                List<String> generos) {
            return new ArtistasCatalogoDTO(id, nombre, imagen, biografia, generos);
        }

    /**
     * Busca el artista cuyo usuario asociado tiene el ID dado,
     * y devuelve el ID del artista.
     */
    public Integer getArtistaIdByUsuarioId(Integer usuarioId) {
        Artistas artista = artistasRepositorio
                .findByUsuario_Id(usuarioId)
                .orElseThrow(() ->
                        new EntityNotFoundException("No existe Artista para usuarioId=" + usuarioId)
                );
        return artista.getId();
    }

    public Artistas getByUsuario(Usuario usuario) {
        return artistasRepositorio.findByUsuario(usuario)
                .orElse(null);
    }


    //Obtiene los artistas asignados a eventos de un promotor específico
    public Page<ArtistasCatalogoDTO> obtenerArtistasPorPromotor(Integer promotorId, Pageable pageable) {
        return artistasRepositorio.findArtistasByPromotorThroughEventos(promotorId, pageable)
                .map(artista -> {
                    List<String> generos = new ArrayList<>();
                    for (GenerosMusicales g : artista.getGenerosMusicales()) {
                        generos.add(g.getNombre());
                    }
                    return new ArtistasCatalogoDTO(
                            artista.getId(),
                            artista.getNombreArtista(),
                            artista.getImagenPerfil(), // Imagen de perfil
                            artista.getBiografia(),    // Biografía
                            generos
                    );
                });
    }


    public List<String> obtenerImagenesDeTodosLosArtistas() {
        return artistasRepositorio.findAll().stream()
                .map(Artistas::getImagenPerfil)
                .collect(Collectors.toList());
    }





}
