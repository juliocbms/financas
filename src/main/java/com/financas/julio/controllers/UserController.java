package com.financas.julio.controllers;

import com.financas.julio.config.TokenConfig;
import com.financas.julio.dto.userDTO.*;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.services.UserServices.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser( @AuthenticationPrincipal User user){
        service.deleteUser(user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserRegisterResponse> updateUSer (@Valid @RequestBody UserUpdateRequest request,  @AuthenticationPrincipal User user){
        User updatedUser = service.updateUser(user.getId(), request);
        UserRegisterResponse response = mapper.toResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserRegisterResponse> findById(@Valid @AuthenticationPrincipal User user){
        User findedUser = service.findById(user.getId());
        UserRegisterResponse response = mapper.toResponse(findedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<UserRegisterResponse>> findAll(User user){
        List<User> findedUsers = service.findAll();
        List<UserRegisterResponse> responses = findedUsers.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }
}
