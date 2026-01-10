package com.financas.julio.repository;

import com.financas.julio.model.Conta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface ContaRepository extends JpaRepository<Conta,Long> {

    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);

    Page<Conta> findByUserId(Long userId, Pageable pageable);

    long countByUserId(Long userId);

    @Query("SELECT COALESCE(SUM(c.saldoAtual), 0) FROM Conta c WHERE c.user.id = :usuarioId")
    BigDecimal getSaldoTotalByUserId(Long usuarioId);
}
