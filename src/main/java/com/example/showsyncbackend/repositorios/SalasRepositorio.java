package com.example.showsyncbackend.repositorios;

import com.example.showsyncbackend.modelos.Salas;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalasRepositorio extends JpaRepository<Salas, Integer> {

    // Método con paginación para filtrar por capacidad
    @Query("SELECT s FROM Salas s WHERE s.capacidad >= :min AND s.capacidad <= :max")
    Page<Salas> filtrarPorCapacidad(@Param("min") Integer min, @Param("max") Integer max, Pageable pageable);

    // Método con paginación para filtrar por capacidad y término de búsqueda
    @Query("SELECT s FROM Salas s WHERE s.capacidad >= :min AND s.capacidad <= :max AND " +
            "(:termino IS NULL OR " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(s.direccion) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(s.provincia) LIKE LOWER(CONCAT('%', :termino, '%')))")
    Page<Salas> filtrarPorCapacidadConTermino(@Param("min") Integer min, @Param("max") Integer max,
                                              @Param("termino") String termino, Pageable pageable);

    // Método con paginación para búsqueda general
    @Query("SELECT s FROM Salas s WHERE " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.direccion) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.provincia) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "CAST(s.capacidad AS string) LIKE CONCAT('%', :filtro, '%')")
    Page<Salas> buscarSalas(@Param("filtro") String filtro, Pageable pageable);

    // Método con paginación para obtener todas las salas
    @Query("SELECT s FROM Salas s WHERE " +
            "(:termino IS NULL OR " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(s.direccion) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(s.provincia) LIKE LOWER(CONCAT('%', :termino, '%')))")
    Page<Salas> findAllConTermino(@Param("termino") String termino, Pageable pageable);

    // Método con paginación para buscar por ciudad
    @Query("SELECT s FROM Salas s WHERE LOWER(s.ciudad) = LOWER(:ciudad)")
    Page<Salas> findByCiudad(@Param("ciudad") String ciudad, Pageable pageable);

    // Método con paginación para buscar por provincia
    @Query("SELECT s FROM Salas s WHERE LOWER(s.provincia) = LOWER(:provincia)")
    Page<Salas> findByProvincia(@Param("provincia") String provincia, Pageable pageable);

    // Métodos sin paginación para casos específicos donde no se necesite
    @Query("SELECT s FROM Salas s WHERE s.capacidad >= :min AND s.capacidad <= :max")
    List<Salas> filtrarPorCapacidadSinPaginacion(@Param("min") Integer min, @Param("max") Integer max);

    @Query("SELECT s FROM Salas s WHERE " +
            "LOWER(s.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.direccion) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.ciudad) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(s.provincia) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "CAST(s.capacidad AS string) LIKE CONCAT('%', :filtro, '%')")
    List<Salas> buscarSalasSinPaginacion(@Param("filtro") String filtro);

    // Buscar sala por ciudad sin paginación
    List<Salas> findByCiudadIgnoreCase(String ciudad);

    // Buscar sala por provincia sin paginación
    List<Salas> findByProvinciaIgnoreCase(String provincia);

    boolean existsByNombreAndDireccion(String nombre, String direccion);

    boolean existsByNombreAndDireccionAndIdNot(String nombre, String direccion, Integer id);


}