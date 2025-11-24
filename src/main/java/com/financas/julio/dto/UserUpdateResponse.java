package com.financas.julio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateResponse(Long id,
                                 String name,
                                 String email,
                                 String password) {
}
