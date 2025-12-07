package com.financas.julio.services.ContaServices;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaUpdateRequest;
import com.financas.julio.dto.userDTO.UserUpdateRequest;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.model.User;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.UserServices.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @InjectMocks
    private ContaService service;

    @Mock
    ContaRepository repository;

    @Mock
    ContaMapper contaMapper;

    @Mock
    UserRepository userRepository;


    @BeforeEach
    void setUp() {
    }

    @Test
    void insertConta() {
        Long usuarioId = 1L;
        Long contaId = 2L;
        BigDecimal saldoInicial = BigDecimal.valueOf(100);
        BigDecimal saldoAtual = saldoInicial;

        User user = new User(usuarioId,
                "julio",
                "julio@email.com",
                "1234",
                null);

        ContaRegisterRequest request = new ContaRegisterRequest(
                usuarioId,
                "Conta Nubank",
                "CORRENTE",
                saldoInicial);

        Conta contaEntity = new Conta(null,
                user,
                "Conta Nubank",
                "CORRENTE",
                saldoInicial,saldoAtual,
                null,
                null,
                null);

        Conta savedConta = new Conta(contaId,
                user,
                "Conta Nubank",
                "CORRENTE",
                saldoInicial,saldoAtual,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());

        Mockito.when(userRepository.existsById(usuarioId)).thenReturn(true);
        Mockito.when(repository.countByUserId(usuarioId)).thenReturn(0L);
        Mockito.when(repository.existsByUserIdAndNameIgnoreCase(request.usuarioId(), request.name())).thenReturn(false);
        Mockito.when(userRepository.getReferenceById(usuarioId)).thenReturn(user);
        Mockito.when(repository.existsByUserIdAndNameIgnoreCase(request.usuarioId(),request.name())).thenReturn(false);
        Mockito.when(contaMapper.toEntity(request)).thenReturn(contaEntity);
        Mockito.when(repository.save(Mockito.any(Conta.class))).thenReturn(savedConta);

        Conta result = service.insertConta(request, usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2L,result.getId());
        Assertions.assertEquals("Conta Nubank",result.getName());
        Assertions.assertEquals("CORRENTE",result.getTipoConta());
        Assertions.assertEquals(user,result.getUser());
        Assertions.assertEquals(BigDecimal.valueOf(100),result.getSaldoAtual());
        Assertions.assertEquals(BigDecimal.valueOf(100),result.getSaldoInicial());

        Mockito.verify(contaMapper).toEntity(request);
        Mockito.verify(repository).save(Mockito.any(Conta.class));

    }

    @Test
    void deleteConta() {
        Long usuarioId = 1L;
        Long contaId = 2L;
        BigDecimal saldoInicial = BigDecimal.valueOf(100);
        BigDecimal saldoAtual = saldoInicial;

        User user = new User(usuarioId,
                "julio",
                "julio@email.com",
                "1234",
                null);

        Conta existingConta = new Conta(contaId,
                user,
                "Conta Nubank",
                "CORRENTE",
                saldoInicial,saldoAtual,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());

        Mockito.when(repository.findById(contaId)).thenReturn(Optional.of(existingConta));

        service.deleteConta(contaId,usuarioId);

        Mockito.verify(repository).findById(contaId);
        Mockito.verify(repository).deleteById(contaId);
    }

    @Test
    void findById() {
        Long usuarioId = 1L;
        Long contaId = 2L;
        BigDecimal saldoInicial = BigDecimal.valueOf(100);
        BigDecimal saldoAtual = saldoInicial;

        User user = new User(usuarioId,
                "julio",
                "julio@email.com",
                "1234",
                null);

        Conta existingConta = new Conta(contaId,
                user,
                "Conta Nubank",
                "CORRENTE",
                saldoInicial,saldoAtual,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());

        Mockito.when(repository.findById(contaId)).thenReturn(Optional.of(existingConta));

        service.findById(contaId,usuarioId);

        Mockito.verify(repository).findById(contaId);

    }

    @Test
    void getSaldoTotal() {
        Long usuarioId = 1L;
        Long contaId = 2L;

        BigDecimal saldoEsperado = BigDecimal.valueOf(100);

        Mockito.when(repository.getSaldoTotalByUserId(usuarioId)).thenReturn(saldoEsperado);

        BigDecimal result = service.getSaldoTotal(usuarioId);

        Assertions.assertEquals(saldoEsperado, result);
        Mockito.verify(repository).getSaldoTotalByUserId(usuarioId);
    }

    @Test
    void updateAccount() {
        Long usuarioId = 1L;
        Long contaId = 2L;
        BigDecimal saldoInicial = BigDecimal.valueOf(100);
        BigDecimal saldoAtual = saldoInicial;

        User user = new User(usuarioId,
                "julio",
                "julio@email.com",
                "1234",
                null);

        ContaUpdateRequest request = new ContaUpdateRequest(
                "Conta Nubank",
                "CORRENTE");

        Conta existing = new Conta(contaId,
                user,
                "Conta Nubank",
                "CORRENTE",
                saldoInicial,
                saldoAtual,
                LocalDateTime.now(),
                LocalDateTime.now(),
                LocalDateTime.now());

        Conta updatedConta = new Conta(contaId,
                user,
                "Conta Banco do Brasil",
                "INVESTIMENTOS",
                new BigDecimal(50),
                new BigDecimal(50),
                existing.getCriadoEm(),
                LocalDateTime.now(),
                LocalDateTime.now());

        Mockito.when(repository.findById(contaId)).thenReturn(Optional.of(existing));

        Mockito.doAnswer(invocation -> {
            ContaUpdateRequest req = invocation.getArgument(0);
            Conta conta = invocation.getArgument(1);
            conta.setName(req.name());
            conta.setTipoConta(req.tipoConta());
            return null;
        }).when(contaMapper).updateToEntity(Mockito.eq(request), Mockito.eq(existing));

        Mockito.when(repository.save(existing)).thenReturn(updatedConta);

        Conta result = service.updateAccount(contaId,request,usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Conta Banco do Brasil",result.getName());
        Assertions.assertEquals("INVESTIMENTOS",result.getTipoConta());

        Mockito.verify(repository).findById(contaId);
        Mockito.verify(contaMapper).updateToEntity(request,existing);
        Mockito.verify(repository).save(existing);
    }

    @Test
    void myAccounts() {
        Long usuarioId = 1L;

        Conta conta1 = new Conta();
        conta1.setId(10L);
        conta1.setName("Nubank");

        Conta conta2 = new Conta();
        conta2.setId(11L);
        conta2.setName("Inter");

        List<Conta> listaEsperada = Arrays.asList(conta1, conta2);

        Mockito.when(userRepository.existsById(usuarioId)).thenReturn(true);

        Mockito.when(repository.findByUserId(usuarioId)).thenReturn(listaEsperada);

        List<Conta> result = service.myAccounts(usuarioId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals("Nubank", result.get(0).getName());
        Assertions.assertEquals("Inter", result.get(1).getName());


        Mockito.verify(repository).findByUserId(usuarioId);
    }
}