package com.financas.julio.dto.categoriaDTO;

import com.financas.julio.model.TipoCategoria;
import jakarta.validation.constraints.Size;

public record CategoriaUpdateRequest(@Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
                                     String name,
                                    TipoCategoria tipoCategoria
) {
}
