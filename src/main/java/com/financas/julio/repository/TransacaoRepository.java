package com.financas.julio.repository;

import com.financas.julio.model.Transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("""
        SELECT t FROM Transacao t
        JOIN FETCH t.conta c
        LEFT JOIN FETCH t.categoria cat
        WHERE t.user.id = :usuarioId
    """)
    List<Transacao> findAllByUSerId(@Param("usuarioId") Long userId);
}
