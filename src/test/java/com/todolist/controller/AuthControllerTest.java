package com.todolist.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.model.User;
import com.todolist.model.dto.AuthRequestDTO;
import com.todolist.repositories.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void loginTest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("test@example.com", "password", "testuser");
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void registerTest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("test@example.com", "password", "testuser");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateProfileTest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("test@example.com", "password", "testuser");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        MvcResult result = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andReturn();

        String token = result.getResponse().getContentAsString();

        User updateRequest = new User();
        updateRequest.setUsername("updatedUser");
        updateRequest.setEmail("updated@example.com");

        mockMvc.perform(put("/auth/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteProfileTest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("test@example.com", "password", "testuser");

        // Registrar o usuário
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Realizar login e obter o token
        String token = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll("\"", "");

        // Excluir o perfil usando o token
        mockMvc.perform(delete("/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }


    @Test
    public void registerWithInvalidEmailTest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("invalid-email", "password", "testuser");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void loginWithInvalidEmailTest() throws Exception {
        AuthRequestDTO request = new AuthRequestDTO("invalid-email", "password", "testuser");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
