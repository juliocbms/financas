package com.financas.julio.repository;

import com.financas.julio.model.TipoCategoria;
import com.financas.julio.model.TipoTransacao;
import com.financas.julio.model.Transacao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("""
    SELECT t FROM Transacao t
    JOIN FETCH t.conta c
    LEFT JOIN FETCH t.categoria cat
    WHERE t.user.id = :usuarioId
    AND (:termo IS NULL OR (LOWER(t.titulo) LIKE :termo OR LOWER(t.descricao) LIKE :termo))
    AND (:tipo IS NULL OR t.tipo = :tipo)
    AND (cast(:dataInicio as date) IS NULL OR t.data >= :dataInicio)
    AND (cast(:dataFim as date) IS NULL OR t.data <= :dataFim)
""")
    Page<Transacao> findAllByFilters(
            @Param("usuarioId") Long userId,
            @Param("termo") String termo,
            @Param("tipo") TipoTransacao tipo,
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable
    );
}
