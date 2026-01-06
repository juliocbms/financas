package com.financas.julio.services.TransacaoServices;

import com.financas.julio.dto.transacaoDTO.TransacaoRegisterRequest;
import com.financas.julio.dto.transacaoDTO.TransacaoUpdateRequest;
import com.financas.julio.mappers.TransacaoMapper;
import com.financas.julio.model.*;
import com.financas.julio.repository.CategoriaRepository;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.TransacaoRepository;
import com.financas.julio.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith; // Removido TestInstance
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @InjectMocks
    private TransacaoService service;

    @Mock
    TransacaoRepository transacaoRepository;

    @Mock
    TransacaoMapper transacaoMapper;

    @Mock
    ContaRepository contaRepository;

    @Mock
    CategoriaRepository categoriaRepository;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void insertTransacao() {
        Long transacaoId = 1L;
        Long usuarioId = 2L;
        Long contaId = 3L;
        Long categoriaId = 4L;

        User user = new User(usuarioId, "julio", "julio@email.com", "1234", null);

        Conta conta = new Conta();
        conta.setId(contaId);
        conta.setName("Nubank");
        conta.setUser(user);
        conta.setSaldoAtual(BigDecimal.ZERO);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setName("Alimentacao");

        TransacaoRegisterRequest request = new TransacaoRegisterRequest(
                contaId,
                categoriaId,
                TipoTransacao.RECEITA,
                new BigDecimal(50),
                null,
                "Titulo transacao",
                "Descricao transacao"
        );

        Transacao transacaoEntity = new Transacao(
                null, user, conta, categoria, TipoTransacao.RECEITA,
                new BigDecimal(50), null, "Titulo transacao", "Descricao transacao", null, null
        );

        Transacao savedTransacao = new Transacao(
                transacaoId, user, conta, categoria, TipoTransacao.RECEITA,
                new BigDecimal(50), LocalDate.now(), "Titulo transacao", "Descricao transacao",
                LocalDateTime.now(), LocalDateTime.now()
        );

        Mockito.when(contaRepository.findById(contaId)).thenReturn(Optional.of(conta));
        Mockito.when(categoriaRepository.existsById(categoriaId)).thenReturn(true);
        Mockito.when(categoriaRepository.getReferenceById(categoriaId)).thenReturn(categoria);
        Mockito.when(userRepository.getReferenceById(usuarioId)).thenReturn(user);

        Mockito.when(transacaoMapper.toEntity(request)).thenReturn(transacaoEntity);

        Mockito.when(contaRepository.save(Mockito.any(Conta.class))).thenReturn(conta);
        Mockito.when(transacaoRepository.save(Mockito.any(Transacao.class))).thenReturn(savedTransacao);

        Transacao result = service.insertTransacao(request, usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(transacaoId, result.getId());
        Assertions.assertEquals("Titulo transacao", result.getTitulo());
        Assertions.assertEquals(new BigDecimal(50), result.getValor());

        Mockito.verify(transacaoRepository).save(Mockito.any(Transacao.class));
        Mockito.verify(contaRepository).save(Mockito.any(Conta.class));
    }

    @Test
    void findById() {
        Long transacaoId = 1L;
        Long usuarioId = 2L;

        User user = new User(usuarioId, "julio", "julio@email.com", "1234", null);
        Transacao transacao = new Transacao();
        transacao.setId(transacaoId);
        transacao.setUser(user);
        transacao.setTitulo("Teste");

        Mockito.when(transacaoRepository.findById(transacaoId)).thenReturn(Optional.of(transacao));

        Transacao result = service.findById(transacaoId, usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(transacaoId, result.getId());
        Mockito.verify(transacaoRepository).findById(transacaoId);
    }

    @Test
    void findAllByUSerId() {
        Long usuarioId = 2L;
        User user = new User(usuarioId, "julio", "julio@email.com", "1234", null);
        Transacao transacao = new Transacao();
        transacao.setUser(user);

        List<Transacao> lista = List.of(transacao);

        Mockito.when(transacaoRepository.findAllByUSerId(usuarioId)).thenReturn(lista);

        List<Transacao> result = service.findAllByUSerId(usuarioId);

        Assertions.assertFalse(result.isEmpty());
        Assertions.assertEquals(1, result.size());
        Mockito.verify(transacaoRepository).findAllByUSerId(usuarioId);
    }

    @Test
    void updateSelfTransacao() {
        Long transacaoId = 1L;
        Long usuarioId = 2L;
        Long contaId = 3L;
        Long categoriaId = 4L;

        User user = new User(usuarioId, "julio", "julio@email.com", "1234", null);

        Conta conta = new Conta();
        conta.setId(contaId);
        conta.setUser(user);
        conta.setSaldoInicial(new BigDecimal("1000.00"));

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);

        Transacao transacaoAntiga = new Transacao();
        transacaoAntiga.setId(transacaoId);
        transacaoAntiga.setUser(user);
        transacaoAntiga.setConta(conta);
        transacaoAntiga.setValor(new BigDecimal(50));

        TransacaoUpdateRequest request = new TransacaoUpdateRequest(
                new BigDecimal(100), "Novo Titulo", "Nova Descricao", null, TipoTransacao.RECEITA, categoriaId, contaId
        );

        Transacao transacaoAtualizada = new Transacao();
        transacaoAtualizada.setId(transacaoId);
        transacaoAtualizada.setUser(user);
        transacaoAtualizada.setValor(new BigDecimal(100));
        transacaoAtualizada.setTitulo("Novo Titulo");


        Mockito.when(transacaoRepository.findById(transacaoId)).thenReturn(Optional.of(transacaoAntiga));
        Mockito.when(transacaoRepository.save(Mockito.any(Transacao.class))).thenReturn(transacaoAtualizada);


        Transacao result = service.updateSelfTransacao(transacaoId, request, usuarioId);

        Assertions.assertEquals(new BigDecimal(100), result.getValor());
        Assertions.assertEquals("Novo Titulo", result.getTitulo());
        Mockito.verify(transacaoRepository).save(Mockito.any(Transacao.class));
    }

    @Test
    void deleteSelfTransacao() {
        Long transacaoId = 1L;
        Long usuarioId = 2L;

        User user = new User(usuarioId, "julio", "julio@email.com", "1234", null);

        Conta conta = new Conta();
        conta.setSaldoAtual(new BigDecimal(100));
        conta.setUser(user);

        Transacao transacao = new Transacao();
        transacao.setId(transacaoId);
        transacao.setUser(user);
        transacao.setConta(conta);
        transacao.setValor(new BigDecimal(50));
        transacao.setTipo(TipoTransacao.RECEITA);


        Mockito.when(transacaoRepository.findById(transacaoId)).thenReturn(Optional.of(transacao));


        Mockito.when(contaRepository.save(Mockito.any(Conta.class))).thenReturn(conta);
        Mockito.doNothing().when(transacaoRepository).delete(transacao);

        service.deleteSelfTransacao(transacaoId, usuarioId);

        Mockito.verify(transacaoRepository).delete(transacao);
        Mockito.verify(contaRepository).save(Mockito.any(Conta.class));
    }
}