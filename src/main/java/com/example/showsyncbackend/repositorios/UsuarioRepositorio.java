package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    // Método para encontrar un usuario por su email
    Optional<Usuario> findByEmail(String email);
    // Método para encontrar un usuario por su nombre de usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    }

