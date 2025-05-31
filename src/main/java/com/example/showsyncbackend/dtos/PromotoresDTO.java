package com.example.showsyncbackend.dtos;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PromotoresDTO {
    private Integer id;
    private Integer usuarioId;
    private String nombrePromotor;
    private String descripcion;
    private String imagenPerfil;


}
