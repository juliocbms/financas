package com.financas.julio.mappers;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.categoriaDTO.CategoriaUpdateRequest;
import com.financas.julio.model.Categoria;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Categoria toEntity(CategoriaRegisterRequest request);

    @Mapping(source = "user.id", target = "usuarioId")
    CategoriaResponse toResponse(Categoria categoria);

    List<CategoriaResponse> toResponseList(List<Categoria> categorias);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateToEntity(CategoriaUpdateRequest request, @MappingTarget Categoria entity);
}
