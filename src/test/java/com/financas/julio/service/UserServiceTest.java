package com.financas.julio.service;

import com.financas.julio.dto.UserRegisterRequest;
import com.financas.julio.dto.UserUpdateRequest;
import com.financas.julio.mappers.UserMapper;
import com.financas.julio.model.User;
import com.financas.julio.repository.UserRepository;
import com.financas.julio.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void insertUser_WhenDataIsValid_ShouldInsertSuccessfully() {
        UserRegisterRequest request = new UserRegisterRequest(
                "julio",
                "julio@email.com",
                "1234"
        );

        User userEntity = new User(
                null,
                "julio",
                "julio@email.com",
                "1234",
                null
        );

        User savedUser = new User(
                1L,
                "julio",
                "julio@email.com",
                "encodedPass",
                LocalDateTime.now()
        );

        Mockito.when(repository.existsByEmail(request.email())).thenReturn(false);
        Mockito.when(userMapper.toEntity(request)).thenReturn(userEntity);
        Mockito.when(passwordEncoder.encode("1234")).thenReturn("encodedPass");
        Mockito.when(repository.save(Mockito.any(User.class))).thenReturn(savedUser);

        User result = service.insertUser(request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1L, result.getId());
        Assertions.assertEquals("julio", result.getName());
        Assertions.assertEquals("encodedPass", result.getPassword());

        Mockito.verify(repository).existsByEmail(request.email());
        Mockito.verify(userMapper).toEntity(request);
        Mockito.verify(passwordEncoder).encode("1234");
        Mockito.verify(repository).save(Mockito.any(User.class));
    }


    @Test
    void deleteUser_WhenIdExists_ShouldDeleteSuccessfully() {
        Long id = 1L;

        User existingUser = new User(id, "julio", "email", "123", LocalDateTime.now());

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingUser));

        service.deleteUser(id);

        Mockito.verify(repository).findById(id);
        Mockito.verify(repository).deleteById(id);
    }



    @Test
    void updateUser_WhenDataIsValid_ShouldUpdateSuccessfully() {
        Long id = 1L;

        UserUpdateRequest request = new UserUpdateRequest(
                "pedro",
                "pedro@email.com",
                "1234"
        );

        User existingUser = new User(
                id,
                "julio",
                "julio@email.com",
                "oldPass",
                LocalDateTime.now()
        );

        User updatedUser = new User(
                id,
                "pedro",
                "pedro@email.com",
                "encodedPass",
                existingUser.getCreatedAt()
        );

        Mockito.when(repository.findById(id)).thenReturn(Optional.of(existingUser));

        Mockito.doAnswer(invocation -> {
            UserUpdateRequest req = invocation.getArgument(0);
            User user = invocation.getArgument(1);
            user.setName(req.name());
            user.setEmail(req.email());
            return null;
        }).when(userMapper).updateToEntity(Mockito.eq(request), Mockito.eq(existingUser));

        Mockito.when(passwordEncoder.encode("1234")).thenReturn("encodedPass");
        Mockito.when(repository.save(existingUser)).thenReturn(updatedUser);

        User result = service.updateUser(id, request);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("pedro", result.getName());
        Assertions.assertEquals("pedro@email.com", result.getEmail());
        Assertions.assertEquals("encodedPass", result.getPassword());

        Mockito.verify(repository).findById(id);
        Mockito.verify(userMapper).updateToEntity(request, existingUser);
        Mockito.verify(passwordEncoder).encode("1234");
        Mockito.verify(repository).save(existingUser);
    }
}
