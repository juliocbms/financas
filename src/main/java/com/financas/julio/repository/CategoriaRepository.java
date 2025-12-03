package com.financas.julio.repository;

import com.financas.julio.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    @Query("SELECT c FROM Categoria c WHERE c.user.id = :userId OR c.user IS NULL")
    List<Categoria> findAllByUserIdOrPublic(@Param("userId") Long userId);

    @Query("SELECT c FROM Categoria c WHERE (c.user.id = :userId OR c.user IS NULL) AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Categoria> findCategoriaByName(@Param("userId") Long userId, @Param("name") String name);
}
