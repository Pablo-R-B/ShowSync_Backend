package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {

    // Método para encontrar un usuario por su email
    Optional<Usuario> findByEmail(String email);

    // Método para encontrar un usuario por su nombre de usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Método para encontrar un usuario por su token de verificación
    Optional<Usuario> findByVerificacionToken(String verificacionToken);

    // Método para encontrar usuarios por su rol
    List<Usuario> findByRol(Rol rol);




}

