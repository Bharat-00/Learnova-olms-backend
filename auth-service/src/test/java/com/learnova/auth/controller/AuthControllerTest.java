package com.learnova.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnova.auth.dto.AuthResponse;
import com.learnova.auth.dto.LoginRequest;
import com.learnova.auth.dto.RegisterRequest;
import com.learnova.auth.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = AuthController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = {
                                com.learnova.auth.security.JwtAuthenticationFilter.class
                        }
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        RegisterRequest request = new RegisterRequest();

        request.setName("Bharat");
        request.setEmail("bharat@gmail.com");
        request.setPassword("password123");

        AuthResponse response =
                new AuthResponse("jwt-token");

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(
                        jsonPath("$.message")
                                .value("User registered successfully")
                );
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequest request = new LoginRequest();

        request.setEmail("bharat@gmail.com");
        request.setPassword("password123");

        AuthResponse response =
                new AuthResponse("jwt-token");

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(
                        jsonPath("$.message")
                                .value("Login successful")
                );
    }
}