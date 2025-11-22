package com.financas.julio.services;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public UserService(UserRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public User insertUser(UserRegisterRequest request){
        User newUser = mapper.toEntity(request);
        return repository.save(newUser);
    }

}
