package com.financas.julio.dto.userDTO;

public record UserUpdateResponse(Long id,
                                 String name,
                                 String email,
                                 String password) {
}
