package com.financas.julio.dto.contaDTO;

import jakarta.validation.constraints.Size;

public record ContaUpdateRequest(@Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
                                 String name,

                                 @Size(max = 100, message = "O tipo da conta deve ter no máximo 100 caracteres")
                                 String tipoConta
) {
}
