package com.financas.julio.services;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.exception.DataBaseException;
import com.financas.julio.services.exception.EmailAlreadyExistsException;
import com.financas.julio.services.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    private Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public User insertUser(UserRegisterRequest request){
        if (repository.existsByEmail(request.email())){
            logger.warn("Email Already Exists");
            throw new EmailAlreadyExistsException(request.email());
        }
        User newUser = mapper.toEntity(request);
        logger.info("Trying to register user");
        try {
            return repository.save(newUser);
        } catch ( IllegalArgumentException e){
            logger.warn("Invalid arguments");
            throw e;
        }catch (DataIntegrityViolationException ex){
                throw new EmailAlreadyExistsException(newUser.getEmail());
        }
    }

    public void deleteUser(Long id){
        logger.info("Searching for id: " + id);
        Optional<User> idToDelete = repository.findById(id);
        if (idToDelete.isEmpty()){
            logger.warn("Resource Not Found with id: " + id);
            throw new ResourceNotFoundException(id);
        }
        try {
            logger.info("Resource with id "+id+ " was deleted");
            repository.deleteById(id);
        } catch ( IllegalArgumentException e){
            logger.warn("Invalid arguments");
            throw e;
        } catch (DataIntegrityViolationException ex){
            throw ex;
        }

    }
}
