package com.financas.julio.repository;

import com.financas.julio.model.Categoria;
import com.financas.julio.model.TipoCategoria;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    @Query("""
    SELECT c
    FROM Categoria c
    WHERE (c.user.id = :userId OR c.user IS NULL)
      AND (:name IS NULL OR LOWER(c.name) LIKE :name) 
      AND (:tipoCategoria IS NULL OR c.tipoCategoria = :tipoCategoria)
""")
    Page<Categoria> findCategoriasFiltradas(
            @Param("userId") Long userId,
            @Param("name") String name,
            @Param("tipoCategoria") TipoCategoria tipoCategoria,
            Pageable pageable
    );
    }
