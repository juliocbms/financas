package com.financas.julio.repository;

import com.financas.julio.model.Transacao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    @Query("SELECT t FROM Transacao t WHERE t.user.id = :userId")
    List<Transacao> findAllByUSerId(@Param("userId") Long userId);
}
