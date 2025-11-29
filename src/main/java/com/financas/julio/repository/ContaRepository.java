package com.financas.julio.repository;

import com.financas.julio.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta,Long> {

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    List<Conta> findByUserId(Long userId);

    long countByUserId(Long userId);
}
