package com.financas.julio.services.UserServices;

import com.financas.julio.dto.userDTO.UserRegisterRequest;
import com.financas.julio.dto.userDTO.UserUpdateRequest;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.exception.EmailAlreadyExistsException;
import com.financas.julio.services.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(UserService.class.getName());

    public UserService(UserRepository repository, UserMapper mapper, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll(){
        logger.info("Finding all People!");
        return repository.findAll();
    }

    @Transactional
    public User insertUser(UserRegisterRequest request){
        if (repository.existsByEmail(request.email())){
            logger.warn("Email Already Exists");
            throw new EmailAlreadyExistsException(request.email());
        }
        User newUser = mapper.toEntity(request);
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
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
        User existingUser = findUserOrThrow(id);
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

    public User findById(Long id){
      return findUserOrThrow(id);
    }

    public User updateUser(Long id, UserUpdateRequest request) {
        User existingUser = findUserOrThrow(id);
        try {
            logger.info("Updating user with id: " + id);
            mapper.updateToEntity(request, existingUser);
            return repository.save(existingUser);
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid arguments");
            throw e;
        } catch (DataIntegrityViolationException ex) {
            logger.error("Data integrity violation", ex);
            throw ex;
        }

    }


    private User findUserOrThrow(Long id) {
        logger.info("Searching for user with id: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("User not found with id: {}", id);
                    return new ResourceNotFoundException(id);
                });
    }
}
