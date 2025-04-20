package com.example.showsyncbackend.dtos;

import lombok.Data;

import java.util.List;

@Data
public class RespuestaPaginacionDTO<T> {
    private List<T> items;
    private int totalPages;
    private int currentPage;
    private int totalItems;
    private int pageSize;
}
