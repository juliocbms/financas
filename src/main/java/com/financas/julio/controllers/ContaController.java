package com.financas.julio.controllers;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.dto.contaDTO.ContaSaldoResponse;
import com.financas.julio.dto.contaDTO.ContaUpdateRequest;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.model.User;
import com.financas.julio.services.ContaServices.ContaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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
    public ResponseEntity<ContaResponse> criarContaParaUmUsuario(@Valid @RequestBody ContaRegisterRequest request, @AuthenticationPrincipal User usuarioLogado){
        Conta insertedConta = contaService.insertConta(request,usuarioLogado.getId());
        ContaResponse response = mapper.toResponse(insertedConta);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id,@AuthenticationPrincipal User usuarioLogado){
        contaService.deleteConta(id,usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/total")
    public ResponseEntity<ContaSaldoResponse> getSaldoTotalByUserId(@Valid @AuthenticationPrincipal User usuarioLogado){
        BigDecimal saldoTotal = contaService.getSaldoTotal(usuarioLogado.getId());
        ContaSaldoResponse response = mapper.saldoToResponse(saldoTotal);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ContaResponse> updateAccount (@Valid @RequestBody ContaUpdateRequest request, @PathVariable Long id,@AuthenticationPrincipal User usuarioLogado){
        Conta updateAccount = contaService.updateAccount(id, request, usuarioLogado.getId());
        ContaResponse response = mapper.toResponse(updateAccount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ContaResponse> findById (@Valid @PathVariable Long id,@AuthenticationPrincipal User usuarioLogado){
        Conta findedConta = contaService.findById(id, usuarioLogado.getId());
        ContaResponse response = mapper.toResponse(findedConta);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<ContaResponse>> accountsForUser(@Valid @AuthenticationPrincipal User usuarioLogado){
        List<Conta> listaConta = contaService.myAccounts(usuarioLogado.getId());
        List<ContaResponse> response = mapper.toResponseList(listaConta);
        return ResponseEntity.ok(response);
    }
}
