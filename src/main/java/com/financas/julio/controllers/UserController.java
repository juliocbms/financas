package com.financas.julio.controllers;

import com.financas.julio.config.TokenConfig;
import com.financas.julio.dto.*;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public UserController(UserService service, UserMapper mapper, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> insertUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest){
        User insertedUser = service.insertUser(userRegisterRequest);
        UserRegisterResponse response = mapper.toResponse(insertedUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request){
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(),request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);
        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return ResponseEntity.ok(new LoginResponse(token));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponse> updateUSer (@Valid @RequestBody UserUpdateRequest request,@PathVariable Long id){
        User updatedUser = service.updateUser(id, request);
        UserUpdateResponse response = mapper.updateTOResponse(updatedUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }
}
