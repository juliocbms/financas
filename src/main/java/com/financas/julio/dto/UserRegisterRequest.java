package com.financas.julio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserRegisterRequest(@NotBlank(message = "Name is mandatory") String name,
                                  @Email @NotBlank(message = "Email is mandatory") String email,
                                  @NotBlank(message = "Password is mandatory") String password) {
}
