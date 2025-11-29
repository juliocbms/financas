package com.financas.julio.controllers;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.services.ContaServices.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;
    private final ContaMapper mapper;

    public ContaController(ContaService contaService, ContaMapper mapper) {
        this.contaService = contaService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> criarContaParaUmUsuario(@Valid @RequestBody ContaRegisterRequest request){
        Conta insertedConta = contaService.insertConta(request);
        ContaResponse response = mapper.toResponse(insertedConta);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
