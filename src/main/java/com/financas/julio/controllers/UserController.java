package com.financas.julio.controllers;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.dto.UserRegisterResponse;
import com.financas.julio.dto.UserUpdateRequest;
import com.financas.julio.dto.UserUpdateResponse;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUSer (@RequestBody UserUpdateRequest request,@PathVariable Long id){
        User updatedUser = service.updateUser(id, request);
        UserUpdateResponse response = mapper.updateTOResponse(updatedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
