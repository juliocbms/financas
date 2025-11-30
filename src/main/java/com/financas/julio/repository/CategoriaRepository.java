package com.financas.julio.repository;

import com.financas.julio.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
}
