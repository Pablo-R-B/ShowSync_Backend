package com.example.showsyncbackend.repositorios;


import com.example.showsyncbackend.modelos.Promotores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotoresRepository extends JpaRepository<Promotores,Integer> {
}
