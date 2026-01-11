package com.financas.julio.controllers;

import com.financas.julio.config.JWTUserData;
import com.financas.julio.config.TokenConfig;
import com.financas.julio.dto.userDTO.*;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.services.UserServices.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;
    private final PagedResourcesAssembler<User> assembler;

    public UserController(UserService service, UserMapper mapper, AuthenticationManager authenticationManager, TokenConfig tokenConfig, PagedResourcesAssembler<User> assembler) {
        this.service = service;
        this.mapper = mapper;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
        this.assembler = assembler;
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
    public ResponseEntity<Void> deleteUser( @AuthenticationPrincipal JWTUserData tokenData){
        service.deleteUser(tokenData.userId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me")
    public ResponseEntity<UserRegisterResponse> updateUSer (@Valid @RequestBody UserUpdateRequest request,  @AuthenticationPrincipal JWTUserData tokenData){
        User updatedUser = service.updateUser(tokenData.userId(), request);
        UserRegisterResponse response = mapper.toResponse(updatedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserRegisterResponse> findById(@Valid @AuthenticationPrincipal JWTUserData tokenData){
        User findedUser = service.findById(tokenData.userId());
        UserRegisterResponse response = mapper.toResponse(findedUser);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserRegisterResponse> findUserById(@Valid @PathVariable Long id){
        User findedUser = service.findById(id);
        UserRegisterResponse response = mapper.toResponse(findedUser);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagedModel<EntityModel<UserRegisterResponse>>>findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "12") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC: Direction.ASC;
        Pageable pageable = PageRequest.of(page,size, Sort.by(sortDirection,"name"));


        Page<User> users = service.findAll(pageable);

        PagedModel<EntityModel<UserRegisterResponse>> response =
                assembler.toModel(
                        users,
                        user -> EntityModel.of(mapper.toResponse(user),linkTo(methodOn(UserController.class)
                                .findUserById(user.getId()))
                                .withSelfRel())
                );

        return ResponseEntity.ok(response);
    }
}
