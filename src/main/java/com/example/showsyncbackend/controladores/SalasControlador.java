package com.example.showsyncbackend.controladores;

import com.example.showsyncbackend.dtos.SalasCatalogoDTO;
import com.example.showsyncbackend.servicios.SalasServicio;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/salas")
@AllArgsConstructor
public class SalasControlador {

    private SalasServicio salasServicio;

//    public SalasControlador(SalasServicio salasServicio) {
//        this.salasServicio = salasServicio;
//    }
    @GetMapping("/mostrarSalas")
    public List<SalasCatalogoDTO> mostrarSalas(){
        List<SalasCatalogoDTO> salas = salasServicio.findAll();
        return salas;
    }

    @GetMapping("/findById")
    public SalasCatalogoDTO findbyId(@RequestParam Integer id){
        SalasCatalogoDTO sala = salasServicio.findbyId(id);
        return sala;
    }
    
}
