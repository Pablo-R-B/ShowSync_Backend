package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.*;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.GenerosMusicales;
import com.example.showsyncbackend.modelos.Usuario;
import com.example.showsyncbackend.repositorios.ArtistasRepositorio;
import com.example.showsyncbackend.repositorios.GenerosMusicalesRepositorio;
import com.example.showsyncbackend.repositorios.UsuarioRepositorio;
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
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private GenerosMusicalesRepositorio generosMusicalesRepositorio;

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
                        art.getMusicUrl(),
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
                        art.getMusicUrl(),
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

    public ArtistaEditarDTO artistaPorId(Integer id) {
        return artistasRepositorio.findByIdWithGeneros(id)
                .map(art -> {
                    List<GenerosMusicalesDTO> generosDto = art.getGenerosMusicales()
                            .stream()
                            .map(gen -> new GenerosMusicalesDTO(gen.getId(), gen.getNombre()))
                            .collect(Collectors.toList());

                    ArtistaEditarDTO dto = new ArtistaEditarDTO();
                    dto.setId(art.getId());
                    dto.setNombreArtista(art.getNombreArtista());
                    dto.setImagenPerfil(art.getImagenPerfil());
                    dto.setBiografia(art.getBiografia());
                    dto.setMusicUrl(art.getMusicUrl());
                    dto.setGenerosMusicales(generosDto);
                    return dto;
                })
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Artista no encontrado")
                );
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
                            artista.getBiografia(),
                            artista.getMusicUrl(),
                            generos
                    );
                });
    }

    public Artistas obtenerArtistaPorId(Integer id) {
        return artistasRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado con ID: " + id));
    }

    @Transactional
    public ArtistaDTO editarDatosArtista(Integer usuarioId, ArtistaEditarDTO datos) {
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Artistas artista = artistasRepositorio.findByUsuario_Id(usuarioId)
                .orElse(null);

        if (artista == null) {
            artista = new Artistas();
            artista.setUsuario(usuario);
        }else {
            artista.setUsuario(usuario);
        }

        artista.setNombreArtista(datos.getNombreArtista());
        artista.setBiografia(datos.getBiografia());
        artista.setImagenPerfil(datos.getImagenPerfil());
        artista.setMusicUrl(datos.getMusicUrl());

        if (datos.getGenerosMusicales() != null) {
            Set<GenerosMusicales> generos = datos.getGenerosMusicales().stream()
                    .map(gDto -> generosMusicalesRepositorio.findById(gDto.getId())
                            .orElseThrow(() -> new RuntimeException("Género no encontrado: " + gDto.getId())))
                    .collect(Collectors.toSet());

            artista.setGenerosMusicales(generos);
        }

        Artistas guardado = artistasRepositorio.saveAndFlush(artista);

       ArtistaDTO dto = new ArtistaDTO();
        dto.setId(guardado.getId());
        dto.setUsuarioId(usuarioId);
        dto.setNombreArtista(guardado.getNombreArtista());
        dto.setBiografia(guardado.getBiografia());
        dto.setImagenPerfil(guardado.getImagenPerfil());
        dto.setMusicUrl(guardado.getMusicUrl());
        return dto;
    }




    public List<String> obtenerImagenesDeTodosLosArtistas() {
        return artistasRepositorio.findAll().stream()
                .map(Artistas::getImagenPerfil)
                .collect(Collectors.toList());
    }

    public List<GenerosMusicalesDTO> obtenerGenerosPorArtistaId(Integer artistaId) {
        Artistas artista = artistasRepositorio.findByIdWithGeneros(artistaId)
                .orElseThrow(() -> new RuntimeException("Artista no encontrado"));

        return artista.getGenerosMusicales()
                .stream()
                .map(genero -> {
                    GenerosMusicalesDTO dto = new GenerosMusicalesDTO();
                    dto.setId(genero.getId());
                    dto.setNombre(genero.getNombre());
                    return dto;
                })
                .collect(Collectors.toList());
    }






}
