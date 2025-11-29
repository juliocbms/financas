package com.financas.julio.controllers;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.dto.contaDTO.ContaUpdateRequest;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.services.ContaServices.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        contaService.deleteConta(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContaResponse> updateAccount (@Valid @RequestBody ContaUpdateRequest request, @PathVariable Long id){
        Conta updateAccount = contaService.updateAccount(id, request);
        ContaResponse response = mapper.toResponse(updateAccount);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContaResponse>> accountsForUser(@Valid @PathVariable Long usuarioId){
        List<Conta> listaConta = contaService.myAccounts(usuarioId);
        List<ContaResponse> response = mapper.toResponseList(listaConta);
        return ResponseEntity.ok(response);
    }
}
