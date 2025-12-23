package com.financas.julio.dto.categoriaDTO;

import com.financas.julio.model.TipoCategoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CategoriaRegisterRequest(
                                       @NotBlank(message = "O nome da categoria é obrigatório")
                                       @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
                                       String name,

                                       @NotNull(message = "O tipo da categoria (RECEITA/DESPESA) é obrigatório")
                                       TipoCategoria tipoCategoria) {
}
