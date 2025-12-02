package com.financas.julio.mappers;

import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.transacaoDTO.TransacaoRegisterRequest;
import com.financas.julio.dto.transacaoDTO.TransacaoResponse;
import com.financas.julio.dto.transacaoDTO.TransacaoUpdateRequest;
import com.financas.julio.model.Categoria;
import com.financas.julio.model.Transacao;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "conta", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    Transacao toEntity(TransacaoRegisterRequest request);

    @Mapping(source = "user.id", target = "usuarioId")
    @Mapping(source = "conta.id", target = "contaId")
    @Mapping(source = "conta.name", target = "contaNome")
    @Mapping(source = "categoria.id", target = "categoriaId")
    @Mapping(source = "categoria.name", target = "categoriaNome")
    TransacaoResponse toResponse(Transacao transacao);

    List<TransacaoResponse> toResponseList(List<Transacao> transacoes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "conta", ignore = true)
    @Mapping(target = "categoria", ignore = true)
    Transacao updateToEntity(TransacaoUpdateRequest request, @MappingTarget Transacao entity);
}
