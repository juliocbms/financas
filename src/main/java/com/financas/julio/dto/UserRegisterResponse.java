package com.financas.julio.dto;

import java.time.LocalDateTime;

public record UserRegisterResponse(String name,
                                   String email,
                                   LocalDateTime dataCadastro) {
}
