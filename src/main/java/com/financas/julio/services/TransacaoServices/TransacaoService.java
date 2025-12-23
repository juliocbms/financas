package com.financas.julio.services.TransacaoServices;

import com.financas.julio.dto.transacaoDTO.TransacaoRegisterRequest;
import com.financas.julio.dto.transacaoDTO.TransacaoUpdateRequest;
import com.financas.julio.mappers.TransacaoMapper;
import com.financas.julio.model.*;
import com.financas.julio.repository.CategoriaRepository;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.TransacaoRepository;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.exception.RegraNegocioException;
import com.financas.julio.services.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final ContaRepository contaRepository;
    private final UserRepository userRepository;
    private final CategoriaRepository categoriaRepository;
    private final TransacaoMapper mapper;

    private final Logger logger = LoggerFactory.getLogger(TransacaoService.class);

    public TransacaoService(TransacaoRepository transacaoRepository, ContaRepository contaRepository, UserRepository userRepository, CategoriaRepository categoriaRepository, TransacaoMapper mapper) {
        this.transacaoRepository = transacaoRepository;
        this.contaRepository = contaRepository;
        this.userRepository = userRepository;
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
    }

    @Transactional
    public Transacao insertTransacao(TransacaoRegisterRequest request, Long usuarioId) {
        Conta conta = contaRepository.findById(request.contaId())
                .orElseThrow(() -> new ResourceNotFoundException(request.contaId()));

        if (!conta.getUser().getId().equals(usuarioId)) {
            throw new RegraNegocioException("Essa conta não pertence a você.");
        }
        Transacao transacao = mapper.toEntity(request);

        if (request.categoriaId() != null) {
            if (!categoriaRepository.existsById(request.categoriaId())) {
                throw new ResourceNotFoundException(request.categoriaId());
            }
            Categoria categoria = categoriaRepository.getReferenceById(request.categoriaId());
            transacao.setCategoria(categoria);
        }

        User user = userRepository.getReferenceById(usuarioId);
        transacao.setUser(user);

        conta.aplicarTransacao(transacao.getTipo(), transacao.getValor());

        transacao.setConta(conta);
        contaRepository.save(conta);

        return transacaoRepository.save(transacao);
    }

    public Transacao findById(Long id, Long usuarioId){
        return buscarTransacaoValidandoDono(id, usuarioId);
    }

    public List<Transacao> findAllByUSerId(Long usuarioId){
        return transacaoRepository.findAllByUSerId(usuarioId);
    }

    @Transactional
    public Transacao updateSelfTransacao(Long transacaoId, TransacaoUpdateRequest request, Long usuarioId){
        Transacao transacao = buscarTransacaoValidandoDono(transacaoId, usuarioId);
        Conta conta = transacao.getConta();

        conta.estornarTransacao(transacao.getTipo(), transacao.getValor());

        mapper.updateToEntity(request, transacao);

        conta.aplicarTransacao(transacao.getTipo(), transacao.getValor());

        contaRepository.save(conta);
        return transacaoRepository.save(transacao);
    }

    @Transactional
    public void deleteSelfTransacao(Long transacaoId, Long usuarioId){
        Transacao transacao = buscarTransacaoValidandoDono(transacaoId,usuarioId);
        Conta conta = transacao.getConta();

        conta.estornarTransacao(transacao.getTipo(), transacao.getValor());

        contaRepository.save(conta);
        transacaoRepository.delete(transacao);

    }


    private Transacao buscarTransacaoValidandoDono(Long transacaoId, Long usuarioLogadoId) {
        Transacao transacao = transacaoRepository.findById(transacaoId)
                .orElseThrow(() -> new ResourceNotFoundException(transacaoId));

        if (transacao.getUser() == null) {
            throw new RegraNegocioException("Você não tem permissão para alterar ou excluir categorias padrão do sistema.");
        }

        if (!transacao.getUser().getId().equals(usuarioLogadoId)) {
            throw new RegraNegocioException("Esta categoria pertence a outro usuário e você não pode mexer nela.");
        }

        return transacao;
    }
}
