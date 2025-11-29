package com.financas.julio.dto.contaDTO;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponse(Long id,
                            Long usuarioId,
                            String nome,
                            String tipoConta,
                            BigDecimal saldoAtual,
                            @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
                            LocalDateTime criadoEm) {
}
