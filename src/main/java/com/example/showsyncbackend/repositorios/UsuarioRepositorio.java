package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.enumerados.Rol;
import com.example.showsyncbackend.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    // Método para buscar usuarios por nombre de usuario o email, ignorando mayúsculas y minúsculas
    Page<Usuario> findByNombreUsuarioContainingIgnoreCaseOrEmailContainingIgnoreCase(String nombreUsuario, String email, Pageable pageable);
    // Método para verificar si un email ya existe
    boolean existsByEmail(String email);

    // Método para verificar si un nombre de usuario ya existe
    boolean existsByNombreUsuario(String nombreUsuario);




    @Query("""
SELECT u FROM Usuario u
WHERE (:rol IS NULL OR u.rol = :rol)
AND (
    :termino IS NULL OR
    u.nombreUsuario LIKE CONCAT('%', CAST(:termino AS string), '%') OR
    u.email LIKE CONCAT('%', CAST(:termino AS string), '%')
)
""")
    Page<Usuario> buscarUsuariosFiltrados(
            @Param("rol") Rol rol,
            @Param("termino") String termino,
            Pageable pageable
    );


    long countByRol(Rol rol);
}

