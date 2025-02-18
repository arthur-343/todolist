package com.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.todolist.model.User;
import com.auth0.jwt.algorithms.Algorithm;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private Algorithm algorithm;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService.setSecret("mySecret");
    }

    @Test
    void generateTokenTest() {
        User user = new User();
        user.setEmail("test@example.com");
        String token = tokenService.generateToken(user);
        assertNotNull(token);
    }

    @Test
    void validateTokenTest() {
        User user = new User();
        user.setEmail("test@example.com");
        String token = tokenService.generateToken(user);
        String email = tokenService.validateToken(token);
        assertEquals("test@example.com", email);
    }

    @Test
    void isTokenExpiredTest() {
        User user = new User();
        user.setEmail("test@example.com");
        String token = tokenService.generateToken(user);
        assertFalse(tokenService.isTokenExpired(token));
    }
}
