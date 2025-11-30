package com.financas.julio.controllers;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.services.CategoriaServices.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
