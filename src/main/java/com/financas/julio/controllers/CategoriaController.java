package com.financas.julio.controllers;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.categoriaDTO.CategoriaUpdateRequest;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.services.CategoriaServices.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final CategoriaMapper mapper;

    public CategoriaController(CategoriaService categoriaService, CategoriaMapper mapper) {
        this.categoriaService = categoriaService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CategoriaResponse> insertCategoria(@Valid @RequestBody CategoriaRegisterRequest request){
        Categoria inserteddCategoria = categoriaService.insertCategoria(request);
        CategoriaResponse response = mapper.toResponse(inserteddCategoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<CategoriaResponse>> listarPorUsuario(@PathVariable Long usuarioId) {

        List<CategoriaResponse> categorias = categoriaService.listarCategorias(usuarioId);

        return ResponseEntity.ok(categorias);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> atualizar( @PathVariable Long id, @RequestParam Long usuarioId,@RequestBody CategoriaUpdateRequest request) {
        Categoria updatedCategoria = categoriaService.updateSelfCategoria(id, request, usuarioId);
        CategoriaResponse response = mapper.toResponse(updatedCategoria);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar( @PathVariable Long id, @RequestParam Long usuarioId) {
        categoriaService.deleteCategoria(id, usuarioId);
        return ResponseEntity.noContent().build();
    }
}
