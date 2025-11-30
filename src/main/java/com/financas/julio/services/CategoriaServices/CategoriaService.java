package com.financas.julio.services.CategoriaServices;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.model.Conta;
import com.financas.julio.model.User;
import com.financas.julio.repository.CategoriaRepository;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.ContaServices.ContaService;
import com.financas.julio.services.exception.RegraNegocioException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper mapper;
    private Logger logger = LoggerFactory.getLogger(ContaService.class.getName());
    private final ContaRepository contaRepository;
    private final UserRepository userRepository;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper mapper, ContaRepository contaRepository, UserRepository userRepository) {
        this.categoriaRepository = categoriaRepository;
        this.mapper = mapper;
        this.contaRepository = contaRepository;
        this.userRepository = userRepository;
    }


    public Categoria insertCategoria(CategoriaRegisterRequest request){
        validarUsuarioExiste(request.usuarioId());
        validarNomeDuplicado(request.usuarioId(), request.name());

        Categoria categoria = mapper.toEntity(request);

        User user = userRepository.getReferenceById(request.usuarioId());
        categoria.setUser(user);
        logger.info("Trying to register a category");
        return categoriaRepository.save(categoria);
    }

    private void validarUsuarioExiste(Long usuarioId) {
        if (!userRepository.existsById(usuarioId)) {
            throw new EntityNotFoundException("Usuário não encontrado com id: " + usuarioId);
        }
    }

    private void validarNomeDuplicado(Long usuarioId, String nome) {
        if (categoriaRepository.existsByUserIdAndNameIgnoreCase(usuarioId, nome)) {
            throw new RegraNegocioException("Você já possui uma categoria chamada '" + nome + "'");
        }
    }
}
