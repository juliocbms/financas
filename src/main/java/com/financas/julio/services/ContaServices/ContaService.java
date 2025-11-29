package com.financas.julio.services.ContaServices;

import com.financas.julio.dto.contaDTO.ContaRegisterRequest;
import com.financas.julio.mappers.ContaMapper;
import com.financas.julio.model.Conta;
import com.financas.julio.model.User;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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


    public Conta insertConta(ContaRegisterRequest request){
        Conta conta = mapper.toEntity(request);

        User usuario = userRepository.getReferenceById(request.usuarioId());
        conta.setUser(usuario);
        return contaRepository.save(conta);
    }
}
