package com.financas.julio.dto.transacaoDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financas.julio.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public record TransacaoResponse(
        Long id,
        Long usuarioId,
        BigDecimal valor,
        TipoTransacao tipo,
        String titulo,
        String descricao,
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,
        Long contaId,
        String contaNome,
        Long categoriaId,
        String categoriaNome,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime criadoEm,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime atualizadoEm
) {
}
