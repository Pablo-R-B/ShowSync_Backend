package com.example.showsyncbackend.utilidades;

import com.example.showsyncbackend.dtos.RespuestaPaginacionDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PaginationUtils {

    public static Pageable createPageable(int page, int size, String sortField, Sort.Direction direction) {
        if (size <= 0) {
            size = 10; // Valor por defecto
        }
        if (sortField == null || sortField.isEmpty()) {
            return PageRequest.of(page, size);
        }
        return PageRequest.of(page, size, direction, sortField);
    }

    public static <T> RespuestaPaginacionDTO<T> toPaginationResponse(Page<T> page) {
        RespuestaPaginacionDTO<T> response = new RespuestaPaginacionDTO<>();
        response.setItems(page.getContent());
        response.setTotalPages(page.getTotalPages());
        response.setCurrentPage(page.getNumber());
        response.setTotalItems((int) page.getTotalElements());
        response.setPageSize(page.getSize());
        return response;
    }
}
