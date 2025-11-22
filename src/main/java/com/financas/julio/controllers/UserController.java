package com.financas.julio.controllers;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.dto.UserRegisterResponse;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;

    public UserController(UserService service, UserMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> insertUser(@RequestBody UserRegisterRequest userRegisterRequest){
        User insertedUser = service.insertUser(userRegisterRequest);
        UserRegisterResponse response = mapper.toResponse(insertedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
