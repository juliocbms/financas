package com.financas.julio.mappers;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.model.Conta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContaMapper {

    @Mapping(target = "usuario", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "criadoEm", ignore = true)
    @Mapping(target = "atualizadoEm", ignore = true)
    @Mapping(target = "saldoAtual", source = "saldoInicial")
    Conta toEntity(ContaRegisterRequest request);

    @Mapping(source = "usuario.id", target = "usuarioId")
    ContaResponse toResponse(Conta conta);
}
