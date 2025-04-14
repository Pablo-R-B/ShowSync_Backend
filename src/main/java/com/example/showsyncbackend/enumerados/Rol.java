package com.example.showsyncbackend.enumerados;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Rol {
    ADMINISTRADOR, ARTISTA, PROMOTOR;

    @JsonCreator
    public static Rol fromString(String value) {
        switch (value.toUpperCase()) {
            case "ARTISTA": return ARTISTA;
            case "PROMOTOR": return PROMOTOR;
            case "ADMINISTRADOR": return ADMINISTRADOR;
            default: throw new IllegalArgumentException("Valor desconocido: " + value);
        }
    }
}
