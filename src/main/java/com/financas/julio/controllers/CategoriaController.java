package com.financas.julio.controllers;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.categoriaDTO.CategoriaUpdateRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.model.Conta;
import com.financas.julio.model.TipoCategoria;
import com.financas.julio.model.User;
import com.financas.julio.services.CategoriaServices.CategoriaService;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper mapper;
    private final PagedResourcesAssembler<Categoria> assembler;

    public CategoriaController(CategoriaService categoriaService, CategoriaMapper mapper, PagedResourcesAssembler<Categoria> assembler) {
        this.categoriaService = categoriaService;
        this.mapper = mapper;
        this.assembler = assembler;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> insertCategoria(@Valid @RequestBody CategoriaRegisterRequest request,@AuthenticationPrincipal User usuarioLogado){
        Categoria inserteddCategoria = categoriaService.insertCategoria(request, usuarioLogado.getId());
        CategoriaResponse response = mapper.toResponse(inserteddCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<PagedModel<EntityModel<CategoriaResponse>>> listarPorUsuario(@AuthenticationPrincipal User usuarioLogado,
           @RequestParam(value = "page", defaultValue = "0") Integer page,
           @RequestParam(required = false) String name,
           @RequestParam(required = false) TipoCategoria tipoCategoria,
           @RequestParam(value = "size", defaultValue = "12") Integer size,
           @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC: Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection,"name"));

        Page<Categoria> categorias = categoriaService.listarCategorias(usuarioLogado.getId(),name,tipoCategoria,pageable);

        PagedModel<EntityModel<CategoriaResponse>> responses =
                assembler.toModel(
                        categorias,
                        categoria -> EntityModel.of(mapper.toResponse(categoria),linkTo(methodOn(CategoriaController.class)
                                .findCategoriaById(categoria.getId(),usuarioLogado))
                                .withSelfRel())
                );

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar(@Valid @PathVariable Long id, @AuthenticationPrincipal User usuarioLogado,@RequestBody CategoriaUpdateRequest request) {
        Categoria updatedCategoria = categoriaService.updateSelfCategoria(id, request, usuarioLogado.getId());
        CategoriaResponse response = mapper.toResponse(updatedCategoria);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@Valid @PathVariable Long id, @AuthenticationPrincipal User usuarioLogado) {
        categoriaService.deleteCategoria(id, usuarioLogado.getId());
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> findCategoriaById (@PathVariable Long id, @AuthenticationPrincipal User usuarioLogado){
        Categoria categoria = categoriaService.findById(id,usuarioLogado.getId());
        CategoriaResponse response = mapper.toResponse(categoria);
        return ResponseEntity.ok(response);
    }
}
