package com.financas.julio.services.ContaServices;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaUpdateRequest;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.model.User;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ContaServiceTest {

    @InjectMocks
    private ContaService service;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ContaMapper mapper;

    @Mock
    private UserRepository userRepository;

    private User user;
    private Conta existingConta;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("Julio");

        existingConta = new Conta();
        existingConta.setId(2L);
        existingConta.setName("Nubank");
        existingConta.setUser(user);
    }

    @Test
    void insertConta() {
        ContaRegisterRequest request = new ContaRegisterRequest("Nubank", "CORRENTE", BigDecimal.ZERO);
        Conta mappedConta = new Conta();
        mappedConta.setName("Nubank");

        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(contaRepository.countByUserId(anyLong())).thenReturn(0L);
        when(contaRepository.existsByUserIdAndNameIgnoreCase(anyLong(), anyString())).thenReturn(false);
        when(userRepository.getReferenceById(anyLong())).thenReturn(user);
        when(mapper.toEntity(any(ContaRegisterRequest.class))).thenReturn(mappedConta);
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Conta result = service.insertConta(request, 1L);

        assertNotNull(result, "A conta resultante não deve ser nula");
        assertEquals(user, result.getUser());
        verify(contaRepository).save(any(Conta.class));
    }

    @Test
    void deleteConta() {
        when(contaRepository.findById(eq(2L))).thenReturn(Optional.of(existingConta));

        assertDoesNotThrow(() -> service.deleteConta(2L, 1L));
        verify(contaRepository).deleteById(2L);
    }

    @Test
    void findById() {
        when(contaRepository.findById(eq(2L))).thenReturn(Optional.of(existingConta));

        Conta result = service.findById(2L, 1L);

        assertNotNull(result);
        assertEquals(2L, result.getId());
    }

    @Test
    void getSaldoTotal() {
        BigDecimal saldo = new BigDecimal("1000.00");
        when(contaRepository.getSaldoTotalByUserId(anyLong())).thenReturn(saldo);

        BigDecimal result = service.getSaldoTotal(1L);

        assertNotNull(result);
        assertEquals(saldo, result);
    }

    @Test
    void updateAccount() {
        ContaUpdateRequest request = new ContaUpdateRequest("Novo Nome", "INVESTIMENTOS");
        when(contaRepository.findById(anyLong())).thenReturn(Optional.of(existingConta));
        when(contaRepository.save(any(Conta.class))).thenReturn(existingConta);

        Conta result = service.updateAccount(2L, request, 1L);

        assertNotNull(result);
        verify(mapper).updateToEntity(any(), any());
        verify(contaRepository).save(existingConta);
    }

    @Test
    void myAccounts() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(contaRepository.findByUserId(anyLong())).thenReturn(Collections.singletonList(existingConta));

        List<Conta> result = service.myAccounts(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }
}