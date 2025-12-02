package com.financas.julio.dto.transacaoDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financas.julio.model.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;


public record TransacaoUpdateRequest(

        @Positive(message = "O valor deve ser positivo")
        BigDecimal valor,

        @Size(max = 150, message = "O titulo pode ter no máximo 150 caracteres")
        @NotBlank(message = "Titulo é obrigatório")
        String titulo,

        @NotBlank(message = "Descrição é obrigatória")
        @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
        String descricao,

        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,

        TipoTransacao tipo,

        Long categoriaId,

        Long contaId
) {
}
