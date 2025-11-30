package com.financas.julio.dto.categoriaDTO;

import com.financas.julio.model.TipoCategoria;

public record CategoriaResponse(Long id,
                                Long usuarioId,
                                String name,
                                TipoCategoria tipoCategoria
){
}
