package com.financas.julio.mappers;

import com.financas.julio.dto.userDTO.UserRegisterRequest;
import com.financas.julio.dto.userDTO.UserRegisterResponse;

import com.financas.julio.dto.userDTO.UserUpdateRequest;

import com.financas.julio.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegisterRequest request);

    @Mapping(source = "createdAt", target = "dataCadastro")
    UserRegisterResponse toResponse(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User updateToEntity(UserUpdateRequest request, @MappingTarget User entity);


}
