package com.financas.julio.controllers;

import com.financas.julio.config.JWTUserData;
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
    public ResponseEntity<EntityModel<ContaResponse>> criarContaParaUmUsuario(@Valid @RequestBody ContaRegisterRequest request, @AuthenticationPrincipal JWTUserData tokenData){
        Conta insertedConta = contaService.insertConta(request,tokenData.userId());
        ContaResponse response = mapper.toResponse(insertedConta);
        EntityModel<ContaResponse> entityModel = EntityModel.of(
                response,
                linkTo(methodOn(ContaController.class).findById(response.id(), tokenData)).withSelfRel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(entityModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id,@AuthenticationPrincipal JWTUserData tokenData){
        contaService.deleteConta(id,tokenData.userId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/usuario/total")
    public ResponseEntity<ContaSaldoResponse> getSaldoTotalByUserId(@Valid @AuthenticationPrincipal JWTUserData tokenData){
        BigDecimal saldoTotal = contaService.getSaldoTotal(tokenData.userId());
        ContaSaldoResponse response = mapper.saldoToResponse(saldoTotal);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<ContaResponse>> updateAccount (@Valid @RequestBody ContaUpdateRequest request, @PathVariable Long id,@AuthenticationPrincipal JWTUserData tokenData){
        Conta updateAccount = contaService.updateAccount(id, request, tokenData.userId());
        ContaResponse response = mapper.toResponse(updateAccount);

        EntityModel<ContaResponse> entityModel = EntityModel.of(
                response,
                linkTo(methodOn(ContaController.class).findById(id, tokenData)).withSelfRel()
        );
        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("{id}")
    public ResponseEntity<EntityModel<ContaResponse>> findById (@Valid @PathVariable Long id,@AuthenticationPrincipal JWTUserData tokenData){
        Conta findedConta = contaService.findById(id, tokenData.userId());
        ContaResponse response = mapper.toResponse(findedConta);

                        EntityModel<ContaResponse> entityModel = EntityModel.of(
                                response,
                                linkTo(methodOn(ContaController.class).findById(id, tokenData)).withSelfRel()
                        );

        return ResponseEntity.ok(entityModel);
    }

    @GetMapping("/usuario")
    public ResponseEntity<PagedModel<EntityModel<ContaResponse>>> accountsForUser(@Valid @AuthenticationPrincipal JWTUserData tokenData,
           @RequestParam(value = "page", defaultValue = "0") Integer page,
           @RequestParam(value = "size", defaultValue = "12") Integer size,
           @RequestParam(value = "direction", defaultValue = "asc") String direction){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC: Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection,"name"));

        Page<Conta> contas = contaService.myAccounts(tokenData.userId(),pageable);

        PagedModel<EntityModel<ContaResponse>> responses =
                assembler.toModel(
                        contas,
                        conta -> EntityModel.of(mapper.toResponse(conta),linkTo(methodOn(ContaController.class)
                                .findById(conta.getId(),tokenData))
                                .withSelfRel())
                );
        return ResponseEntity.ok(responses);
    }
}
