package com.financas.julio.controllers;

import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.transacaoDTO.TransacaoRegisterRequest;
import com.financas.julio.dto.transacaoDTO.TransacaoResponse;
import com.financas.julio.dto.transacaoDTO.TransacaoUpdateRequest;
import com.financas.julio.mappers.TransacaoMapper;
import com.financas.julio.model.*;
import com.financas.julio.services.TransacaoServices.TransacaoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {

    private final TransacaoService transacaoService;
    private final TransacaoMapper transacaoMapper;
    private final PagedResourcesAssembler<Transacao> assembler;

    public TransacaoController(TransacaoService transacaoService, TransacaoMapper transacaoMapper, PagedResourcesAssembler<Transacao> assembler) {
        this.transacaoService = transacaoService;
        this.transacaoMapper = transacaoMapper;
        this.assembler = assembler;
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
    public ResponseEntity<PagedModel<EntityModel<TransacaoResponse>>> findAllById(@Valid@AuthenticationPrincipal User usuarioLogado,
           @RequestParam(value = "page", defaultValue = "0") Integer page,
           @RequestParam(value = "size", defaultValue = "12") Integer size,
           @RequestParam(required = false) String termo,
           @RequestParam(required = false) TipoTransacao tipo,
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
           @RequestParam(value = "direction", defaultValue = "asc") String direction){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC: Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection,"titulo"));

        Page<Transacao> transacoes = transacaoService.listarComFiltros(usuarioLogado.getId(),
                termo,
                tipo,
                dataInicio,
                dataFim,
                pageable);

        PagedModel<EntityModel<TransacaoResponse>> responses =
                assembler.toModel(
                        transacoes,
                        transacao -> EntityModel.of(transacaoMapper.toResponse(transacao),linkTo(methodOn(CategoriaController.class)
                                .findCategoriaById(transacao.getId(),usuarioLogado))
                                .withSelfRel())
                );

        return ResponseEntity.ok(responses);
    }
}
