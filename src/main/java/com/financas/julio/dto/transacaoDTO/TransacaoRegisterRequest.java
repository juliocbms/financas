package com.financas.julio.dto.transacaoDTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.financas.julio.model.TipoTransacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;


public record TransacaoRegisterRequest(

        @NotNull(message = "O ID do usuário é obrigatório")
        Long usuarioId,

        @NotNull(message = "A conta é obrigatória")
        Long contaId,

        Long categoriaId,

        @NotNull(message = "O tipo da transação é obrigatório")
        TipoTransacao tipo,

        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor da transação deve ser positivo")
        BigDecimal valor,

        @NotNull(message = "A data da transação é obrigatória")
        @JsonFormat(pattern = "dd/MM/yyyy")
        LocalDate data,
        @Size(max = 150, message = "O titulo pode ter no máximo 150 caracteres")
        String titulo,

        @Size(max = 255, message = "A descrição pode ter no máximo 255 caracteres")
        String descricao
) {
}
