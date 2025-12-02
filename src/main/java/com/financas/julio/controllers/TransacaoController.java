package com.financas.julio.controllers;

import com.financas.julio.dto.transacaoDTO.TransacaoRegisterRequest;
import com.financas.julio.dto.transacaoDTO.TransacaoResponse;
import com.financas.julio.mappers.TransacaoMapper;
import com.financas.julio.model.Transacao;
import com.financas.julio.services.TransacaoServices.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final TransacaoMapper transacaoMapper;

    public TransacaoController(TransacaoService transacaoService, TransacaoMapper transacaoMapper) {
        this.transacaoService = transacaoService;
        this.transacaoMapper = transacaoMapper;
    }

    @PostMapping
    public ResponseEntity<TransacaoResponse> criarUmaTransacaoParaUmUsuario(@Valid @RequestBody TransacaoRegisterRequest request){
        Transacao insertedTransacao = transacaoService.insertTransacao(request);
        TransacaoResponse response = transacaoMapper.toResponse(insertedTransacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
