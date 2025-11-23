package com.financas.julio.services;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.exception.DataBaseException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


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
        try {
            User newUser = mapper.toEntity(request);
            logger.info("Trying to register an user with ID:" + newUser.getId());
            return repository.save(newUser);
        } catch ( IllegalArgumentException e){
            logger.warn("Invalid arguments");
            throw e;
        }catch ( DataBaseException e){
            logger.error("An error ocurre in Database");
            throw e;
        }

    }

}
