package com.financas.julio.mappers;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.dto.contaDTO.ContaUpdateRequest;
import com.financas.julio.model.Conta;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContaMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "saldoAtual", source = "saldoInicial")
    Conta toEntity(ContaRegisterRequest request);

    @Mapping(source = "user.id", target = "usuarioId")
    ContaResponse toResponse(Conta conta);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "saldoInicial", ignore = true)
    @Mapping(target = "saldoAtual", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    Conta updateToEntity(ContaUpdateRequest request, @MappingTarget Conta entity);

    List<ContaResponse> toResponseList(List<Conta> contas);
}
