package com.example.showsyncbackend.servicios;

import com.example.showsyncbackend.dtos.SalasCatalogoDTO;
import com.example.showsyncbackend.modelos.Salas;
import com.example.showsyncbackend.repositorios.Salasrepositorio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SalasServicio {

    @Autowired
    private Salasrepositorio salasrepositorio;



    /**
     * Obtener todas las Salas
     * @return
     */
    public List<SalasCatalogoDTO> findAll(){
        List<SalasCatalogoDTO> salasCatalogoDTOS= new ArrayList<>();
        List<Salas> salasTotal = salasrepositorio.findAll();

        for (Salas s : salasTotal){
            SalasCatalogoDTO salasCatalogoDTO = new SalasCatalogoDTO();
            salasCatalogoDTO.setCapacidad(s.getCapacidad());
            salasCatalogoDTO.setDescripcion(s.getDescripcion());
            salasCatalogoDTO.setNombre(s.getNombre());
            salasCatalogoDTO.setImagen(s.getImagen());
            salasCatalogoDTO.setDireccion(s.getDireccion());

            salasCatalogoDTOS.add(salasCatalogoDTO);
        }
        return salasCatalogoDTOS;
    }

    /**
     * Obtener Sala por id
     *
     */
    public SalasCatalogoDTO findbyId(Integer id){
        SalasCatalogoDTO salasCatalogoDTO = new SalasCatalogoDTO();
        Salas sala = salasrepositorio.getReferenceById(id);

        salasCatalogoDTO.setNombre(sala.getNombre());
        salasCatalogoDTO.setDireccion(sala.getDireccion());
        salasCatalogoDTO.setDescripcion(sala.getDescripcion());
        salasCatalogoDTO.setCapacidad(sala.getCapacidad());
        salasCatalogoDTO.setImagen(sala.getImagen());

        return salasCatalogoDTO;
    }

}
