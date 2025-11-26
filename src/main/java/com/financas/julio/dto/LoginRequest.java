package com.financas.julio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@Email @NotBlank(message = "Email is mandatory") String email,
                           @NotBlank(message = "Password is mandatory") String password) {
}
