package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
}
