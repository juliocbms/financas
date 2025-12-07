package com.financas.julio.controllers;

import com.financas.julio.dto.transacaoDTO.TransacaoRegisterRequest;
import com.financas.julio.dto.transacaoDTO.TransacaoResponse;
import com.financas.julio.dto.transacaoDTO.TransacaoUpdateRequest;
import com.financas.julio.mappers.TransacaoMapper;
import com.financas.julio.model.Transacao;
import com.financas.julio.model.User;
import com.financas.julio.services.TransacaoServices.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<TransacaoResponse> criarUmaTransacaoParaUmUsuario(@Valid @RequestBody TransacaoRegisterRequest request,@AuthenticationPrincipal User usuarioLogado){
        Transacao insertedTransacao = transacaoService.insertTransacao(request,usuarioLogado.getId());
        TransacaoResponse response = transacaoMapper.toResponse(insertedTransacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponse> editarSuaPropriaTransacao(@Valid @PathVariable Long id,@AuthenticationPrincipal User usuarioLogado, @RequestBody TransacaoUpdateRequest request){
        Transacao updatedTransacao = transacaoService.updateSelfTransacao(id,request,usuarioLogado.getId());
        TransacaoResponse response = transacaoMapper.toResponse(updatedTransacao);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Valid @PathVariable Long id, @AuthenticationPrincipal User usuarioLogado){
        transacaoService.deleteSelfTransacao(id,usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransacaoResponse> findById(@Valid @PathVariable Long id, @AuthenticationPrincipal User usuarioLogado){
        Transacao transacao = transacaoService.findById(id, usuarioLogado.getId());
        TransacaoResponse response = transacaoMapper.toResponse(transacao);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<TransacaoResponse>> findAllById(@Valid@AuthenticationPrincipal User usuarioLogado){
        List<Transacao> transacaos = transacaoService.findAllByUSerId(usuarioLogado.getId());
        List<TransacaoResponse> responses = transacaoMapper.toResponseList(transacaos);
        return ResponseEntity.ok(responses);
    }
}
