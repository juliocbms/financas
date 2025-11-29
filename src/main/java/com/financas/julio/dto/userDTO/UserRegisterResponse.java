package com.financas.julio.dto.userDTO;

import java.time.LocalDateTime;

public record UserRegisterResponse(String name,
                                   String email,
                                   LocalDateTime dataCadastro) {
}
