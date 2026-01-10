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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/contas")
public class ContaController {

    private final ContaService contaService;
    private final ContaMapper mapper;
    private final PagedResourcesAssembler<Conta> assembler;

    public ContaController(ContaService contaService, ContaMapper mapper, PagedResourcesAssembler<Conta> assembler) {
        this.contaService = contaService;
        this.mapper = mapper;
        this.assembler = assembler;
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
    public ResponseEntity<PagedModel<EntityModel<ContaResponse>>> accountsForUser(@Valid @AuthenticationPrincipal User usuarioLogado,
           @RequestParam(value = "page", defaultValue = "0") Integer page,
           @RequestParam(value = "size", defaultValue = "12") Integer size,
           @RequestParam(value = "direction", defaultValue = "asc") String direction){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC: Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection,"name"));

        Page<Conta> contas = contaService.myAccounts(usuarioLogado.getId(),pageable);

        PagedModel<EntityModel<ContaResponse>> responses =
                assembler.toModel(
                        contas,
                        conta -> EntityModel.of(mapper.toResponse(conta),linkTo(methodOn(ContaController.class)
                                .findById(conta.getId(),usuarioLogado))
                                .withSelfRel())
                );
        return ResponseEntity.ok(responses);
    }
}
