package com.financas.julio.dto.contaDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ContaRegisterRequest(@NotNull(message = "O ID do usuário é obrigatório")
                                   Long usuarioId,

                                   @NotBlank(message = "O nome da conta não pode estar vazio")
                                   @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
                                   String nome,

                                   @NotBlank(message = "O tipo da conta não pode estar vazio")
                                   @Size(max = 100, message = "O tipo da conta deve ter no máximo 100 caracteres")
                                   String tipoConta,

                                   @PositiveOrZero(message = "O saldo inicial não pode ser negativo")
                                   BigDecimal saldoInicial
) {
}
