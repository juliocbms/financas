package com.financas.julio.services.ContaServices;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.dto.contaDTO.ContaResponse;
import com.financas.julio.dto.contaDTO.ContaUpdateRequest;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.model.User;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.exception.RegraNegocioException;
import com.financas.julio.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ContaMapper mapper;
    private Logger logger = LoggerFactory.getLogger(ContaService.class.getName());
    private final UserRepository userRepository;

    public ContaService(ContaRepository contaRepository, ContaMapper mapper, UserRepository userRepository) {
        this.contaRepository = contaRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    private static final int LIMITE_MAXIMO_CONTAS = 10;


    @Transactional
    public Conta insertConta(ContaRegisterRequest request){
        validarUsuarioExiste(request.usuarioId());
        validarLimiteDeContas(request.usuarioId());
        validarNomeDuplicado(request.usuarioId(), request.name());

        User usuario = userRepository.getReferenceById(request.usuarioId());
        Conta conta = mapper.toEntity(request);
        conta.setUser(usuario);
        logger.info("Trying to register a user account");
            return contaRepository.save(conta);
    }

    @Transactional
    public void deleteConta(Long id){
        Conta existingAccount = findAccountOrThrow(id);
        if (existingAccount.getId() == null){
            logger.info("Resource with id "+id+ " was deleted");
            contaRepository.deleteById(id);
        }
        try {
            logger.info("Resource with id "+id+ " was deleted");
            contaRepository.deleteById(id);
        } catch (IllegalArgumentException e){
            logger.warn("Invalid arguments");
            throw e;
        } catch (DataIntegrityViolationException ex){
            throw ex;
        }
    }

    public Conta findById(Long id){
        return contaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public BigDecimal getSaldoTotal(Long userId){
        return contaRepository.getSaldoTotalByUserId(userId);
    }

    public Conta updateAccount(Long id, ContaUpdateRequest request){
        Conta existingAccount = findAccountOrThrow(id);
        try {
            logger.info("Updating account with id: " + id);
            mapper.updateToEntity(request, existingAccount);
            return contaRepository.save(existingAccount);
        } catch (IllegalArgumentException e){
            logger.warn("Invalid arguments");
            throw e;
        } catch (DataIntegrityViolationException ex) {
            logger.error("Data integrity violation", ex);
            throw ex;
        }
    }

    public List<Conta> myAccounts( Long usuarioId){
        validarUsuarioExiste(usuarioId);
        return contaRepository.findByUserId(usuarioId);
    }


    private void validarUsuarioExiste(Long usuarioId) {
        if (!userRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuário não encontrado com o ID: " + usuarioId);
        }
    }

    private void validarLimiteDeContas(Long usuarioId) {
        long totalAtual = contaRepository.countByUserId(usuarioId);

        if (totalAtual >= LIMITE_MAXIMO_CONTAS) {
            throw new RegraNegocioException(
                    String.format("Limite atingido. O usuário já possui %d contas (Máximo: %d)", totalAtual, LIMITE_MAXIMO_CONTAS)
            );
        }
    }

    private void validarNomeDuplicado(Long usuarioId, String nomeConta) {
        if (contaRepository.existsByUserIdAndNameIgnoreCase(usuarioId, nomeConta)) {
            throw new RegraNegocioException("Já existe uma conta cadastrada com o nome '" + nomeConta + "'");
        }
    }

    private Conta findAccountOrThrow(Long id){
        logger.info("Searching for account with id: {}", id);
        return contaRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Account not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
    }
}
