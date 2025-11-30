package com.financas.julio.services.CategoriaServices;

import com.financas.julio.dto.categoriaDTO.CategoriaRegisterRequest;
import com.financas.julio.dto.categoriaDTO.CategoriaResponse;
import com.financas.julio.dto.categoriaDTO.CategoriaUpdateRequest;
import com.financas.julio.mappers.CategoriaMapper;
import com.financas.julio.model.Categoria;
import com.financas.julio.model.User;
import com.financas.julio.repository.CategoriaRepository;
import com.financas.julio.repository.ContaRepository;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.ContaServices.ContaService;
import com.financas.julio.services.exception.RegraNegocioException;
import com.financas.julio.services.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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


    @Transactional
    public Categoria insertCategoria(CategoriaRegisterRequest request){
        validarUsuarioExiste(request.usuarioId());
        validarNomeDuplicado(request.usuarioId(), request.name());

        Categoria categoria = mapper.toEntity(request);

        User user = userRepository.getReferenceById(request.usuarioId());
        categoria.setUser(user);
        logger.info("Trying to register a category");
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public void deleteCategoria(Long categoriaId, Long usuarioLogadoId) {
        Categoria categoria = buscarCategoriaValidandoDono(categoriaId, usuarioLogadoId);

        categoriaRepository.delete(categoria);
    }


    @Transactional
    public Categoria updateSelfCategoria(Long categoriaId, CategoriaUpdateRequest request, Long usuarioLogadoId){
        Categoria categoria = buscarCategoriaValidandoDono(categoriaId, usuarioLogadoId);
        if (request.name() != null && !request.name().equals(categoria.getName())) {
            validarNomeDuplicado(usuarioLogadoId, request.name());
        }
        mapper.updateToEntity(request, categoria);
        return categoriaRepository.save(categoria);
    }

    public Categoria findById(Long categoriaId, Long usuarioId){
        Categoria categoria = buscarCategoriaValidandoDono(categoriaId,usuarioId);
       return categoriaRepository.findById(categoriaId).orElseThrow(() -> new ResourceNotFoundException(categoriaId));
    }

    public List<CategoriaResponse> listarCategorias(Long userId) {
        List<Categoria> categorias = categoriaRepository.findAllByUserIdOrPublic(userId);
        return mapper.toResponseList(categorias);
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

    private Categoria buscarCategoriaValidandoDono(Long categoriaId, Long usuarioLogadoId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException(categoriaId));

        if (categoria.getUser() == null) {
            throw new RegraNegocioException("Você não tem permissão para alterar ou excluir categorias padrão do sistema.");
        }

        if (!categoria.getUser().getId().equals(usuarioLogadoId)) {
            throw new RegraNegocioException("Esta categoria pertence a outro usuário e você não pode mexer nela.");
        }

        return categoria;
    }
}
