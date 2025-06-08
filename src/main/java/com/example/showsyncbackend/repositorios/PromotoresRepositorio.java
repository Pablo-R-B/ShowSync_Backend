package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.Promotores;
import com.example.showsyncbackend.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotoresRepositorio extends JpaRepository<Promotores, Integer> {
    Optional<Promotores> findByUsuarioId(Integer usuarioId);
    Optional<Promotores> findById(Integer id);
    Optional<Promotores> findByUsuario(Usuario usuario);

    // Añade este método para búsqueda por nombre con paginación
    Page<Promotores> findByNombrePromotorContainingIgnoreCase(String nombre, Pageable pageable);
}
