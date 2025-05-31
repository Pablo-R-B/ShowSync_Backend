package com.example.showsyncbackend.utilidades;

import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Artistas;
import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.modelos.Usuario;

import java.util.HashMap;
import java.util.Map;

public class PerfilMapeador {
    public static Map<String, Object> mapearPerfilUsuario(Usuario usuario) {
        Map<String, Object> datos = new HashMap<>();
        datos.put("id", usuario.getId());
        datos.put("email", usuario.getEmail());
        datos.put("nombre", usuario.getNombreUsuario());
        datos.put("rol", usuario.getRol());

        if (usuario.getRol() == Rol.ARTISTA && usuario.getArtista() != null) {
            Artistas artista = usuario.getArtista();
            datos.put("nombreArtista", artista.getNombreArtista());
            datos.put("biografia", artista.getBiografia());
            datos.put("musicUrl", artista.getMusicUrl());
            datos.put("imagenPerfil", artista.getImagenPerfil());
        } else if (usuario.getRol() == Rol.PROMOTOR && usuario.getPromotor() != null) {
            Promotores promotor = usuario.getPromotor();
            datos.put("nombrePromotor", promotor.getNombrePromotor());
            datos.put("descripcion", promotor.getDescripcion());
            datos.put("imagenPerfil", promotor.getImagenPerfil());
        }

        return datos;
    }
}
