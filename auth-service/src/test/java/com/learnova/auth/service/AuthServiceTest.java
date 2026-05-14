package com.learnova.auth.service;

import com.learnova.auth.dto.AuthResponse;
import com.learnova.auth.dto.LoginRequest;
import com.learnova.auth.dto.RegisterRequest;
import com.learnova.auth.entity.User;
import com.learnova.auth.enums.Role;
import com.learnova.auth.exception.BadRequestException;
import com.learnova.auth.repository.UserRepository;
import com.learnova.auth.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {

        registerRequest = new RegisterRequest();
        registerRequest.setName("Bharat");
        registerRequest.setEmail("bharat@gmail.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("bharat@gmail.com");
        loginRequest.setPassword("password123");
    }

    @Test
    void shouldRegisterUserSuccessfully() {

        when(userRepository.existsByEmail(anyString()))
                .thenReturn(false);

        when(passwordEncoder.encode(anyString()))
                .thenReturn("encodedPassword");

        when(jwtService.generateToken(anyString(), anyString()))
                .thenReturn("jwt-token");

        User savedUser = User.builder()
                .id(1L)
                .name("Bharat")
                .email("bharat@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        AuthResponse response =
                authService.register(registerRequest);

        assertNotNull(response);

        assertEquals("jwt-token", response.getToken());

        verify(userRepository, times(1))
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(anyString()))
                .thenReturn(true);

        assertThrows(
                BadRequestException.class,
                () -> authService.register(registerRequest)
        );

        verify(userRepository, never())
                .save(any(User.class));
    }

    @Test
    void shouldLoginSuccessfully() {

        User user = User.builder()
                .id(1L)
                .name("Bharat")
                .email("bharat@gmail.com")
                .password("encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(anyString()))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(anyString(), anyString()))
                .thenReturn(true);

        when(jwtService.generateToken(anyString(), anyString()))
                .thenReturn("jwt-token");

        AuthResponse response =
                authService.login(loginRequest);

        assertNotNull(response);

        assertEquals("jwt-token", response.getToken());
    }
}