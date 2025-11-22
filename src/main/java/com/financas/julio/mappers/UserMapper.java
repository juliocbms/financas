package com.financas.julio.mappers;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.dto.UserRegisterResponse;

import com.financas.julio.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toEntity(UserRegisterRequest request);
    UserRegisterResponse toResponse(User user);
}
