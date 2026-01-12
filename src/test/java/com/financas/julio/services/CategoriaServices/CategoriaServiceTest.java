package com.financas.julio.services.CategoriaServices;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.categoriaDTO.CategoriaUpdateRequest;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.model.TipoCategoria;
import com.financas.julio.model.User;
import com.financas.julio.repository.CategoriaRepository;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @InjectMocks
    private CategoriaService service;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private CategoriaMapper mapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ContaRepository contaRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void insertCategoria() {
        Long usuarioId = 1L;
        String nomeCategoria = "Lazer";

        CategoriaRegisterRequest request = new CategoriaRegisterRequest( nomeCategoria, TipoCategoria.RECEITA);
        User user = new User();
        user.setId(usuarioId);

        Categoria categoria = new Categoria();
        categoria.setName(nomeCategoria);
        categoria.setUser(user);

        Mockito.when(categoriaRepository.existsByUserIdAndNameIgnoreCase(usuarioId, nomeCategoria)).thenReturn(false);

        Mockito.when(mapper.toEntity(request)).thenReturn(categoria);

        Mockito.when(userRepository.getReferenceById(usuarioId)).thenReturn(user);

        Mockito.when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria result = service.insertCategoria(request, usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(nomeCategoria, result.getName());
        Assertions.assertEquals(usuarioId, result.getUser().getId());

        Mockito.verify(categoriaRepository).save(categoria);
    }

    @Test
    void deleteCategoria() {
        Long categoriaId = 10L;
        Long usuarioId = 1L;

        User user = new User();
        user.setId(usuarioId);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setUser(user);

        Mockito.when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        Mockito.doNothing().when(categoriaRepository).delete(categoria);

        service.deleteCategoria(categoriaId, usuarioId);

        Mockito.verify(categoriaRepository).delete(categoria);
    }

    @Test
    void updateSelfCategoria() {
        Long categoriaId = 10L;
        Long usuarioId = 1L;
        String novoNome = "Lazer Atualizado";

        User user = new User();
        user.setId(usuarioId);

        Categoria categoriaAntiga = new Categoria();
        categoriaAntiga.setId(categoriaId);
        categoriaAntiga.setName("Lazer Antigo");
        categoriaAntiga.setUser(user);

        CategoriaUpdateRequest request = new CategoriaUpdateRequest(novoNome,TipoCategoria.RECEITA);

        Mockito.when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoriaAntiga));

        Mockito.when(categoriaRepository.existsByUserIdAndNameIgnoreCase(usuarioId, novoNome)).thenReturn(false);

        Mockito.when(categoriaRepository.save(categoriaAntiga)).thenReturn(categoriaAntiga);

        Categoria result = service.updateSelfCategoria(categoriaId, request, usuarioId);

        Mockito.verify(mapper).updateToEntity(request, categoriaAntiga);
        Mockito.verify(categoriaRepository).save(categoriaAntiga);
        Assertions.assertNotNull(result);
    }

    @Test
    void findById() {
        Long categoriaId = 10L;
        Long usuarioId = 1L;

        User user = new User();
        user.setId(usuarioId);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setUser(user);

        Mockito.when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        Categoria result = service.findById(categoriaId, usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(categoriaId, result.getId());
    }


    @Test
    void listarCategorias() {
        Long usuarioId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        Categoria cat1 = new Categoria();
        cat1.setId(10L);
        cat1.setName("teste");

        List<Categoria> listaEntidades = List.of(cat1);
        Page<Categoria> pageRetorno = new PageImpl<>(listaEntidades);

        Mockito.when(categoriaRepository.findCategoriasFiltradas(
                Mockito.eq(usuarioId),
                Mockito.isNull(),
                Mockito.isNull(),
                Mockito.eq(pageable))
        ).thenReturn(pageRetorno);

        Page<Categoria> result = service.listarCategorias(usuarioId, null, null, pageable);

        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.getTotalElements());
        Assertions.assertEquals(10L, result.getContent().get(0).getId());

        Mockito.verify(categoriaRepository, Mockito.times(1))
                .findCategoriasFiltradas(usuarioId, null, null, pageable);
    }
}