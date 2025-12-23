package com.financas.julio.controllers;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.categoriaDTO.CategoriaUpdateRequest;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.model.User;
import com.financas.julio.services.CategoriaServices.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper mapper;

    public CategoriaController(CategoriaService categoriaService, CategoriaMapper mapper) {
        this.categoriaService = categoriaService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> insertCategoria(@Valid @RequestBody CategoriaRegisterRequest request,@AuthenticationPrincipal User usuarioLogado){
        Categoria inserteddCategoria = categoriaService.insertCategoria(request, usuarioLogado.getId());
        CategoriaResponse response = mapper.toResponse(inserteddCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoriaResponse>> listarPorUsuario(@AuthenticationPrincipal User usuarioLogado) {

        List<CategoriaResponse> categorias = categoriaService.listarCategorias(usuarioLogado.getId());

        return ResponseEntity.ok(categorias);
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

    @GetMapping("/buscar")
    public ResponseEntity<List<CategoriaResponse>> findCategoriaByName(@AuthenticationPrincipal User usuarioLogado, @RequestParam String name){

        List<Categoria> categorias = categoriaService.findByName(usuarioLogado.getId(), name);
        List<CategoriaResponse> response = categorias.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> findCategoriaById (@PathVariable Long id, @AuthenticationPrincipal User usuarioLogado){
        Categoria categoria = categoriaService.findById(id,usuarioLogado.getId());
        CategoriaResponse response = mapper.toResponse(categoria);
        return ResponseEntity.ok(response);
    }
}
