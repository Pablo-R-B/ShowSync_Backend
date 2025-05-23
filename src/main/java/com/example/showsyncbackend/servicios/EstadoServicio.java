package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.enumerados.Estado;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstadoServicio {

    public List<String> obtenerTodos() {
        return Arrays.stream(Estado.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
