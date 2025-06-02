package com.example.showsyncbackend.utilidades;


import com.example.showsyncbackend.dtos.SalaDTO;
import com.example.showsyncbackend.modelos.Salas;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SalaMapper {
    SalaMapper INSTANCE = Mappers.getMapper(SalaMapper.class);

    @Mapping(source = "administrador.id", target = "id") // Si necesitas mapear el ID del admin
    SalaDTO salaToSalaDTO(Salas sala);

    @Mapping(target = "administrador", ignore = true) // Normalmente se maneja aparte
    Salas salaDTOToSala(SalaDTO salaDTO);
}