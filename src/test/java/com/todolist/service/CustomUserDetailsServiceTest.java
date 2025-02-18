package com.todolist.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.todolist.model.User;
import com.todolist.repositories.UserRepository;

public class CustomUserDetailsServiceTest {

    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Mock
    private UserRepository repository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void loadUserByUsernameTest_UserExists() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");

        when(repository.findByEmail("test@example.com")).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");
        assertNotNull(userDetails);
        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    public void loadUserByUsernameTest_UserNotExists() {
        when(repository.findByEmail("nonexistent@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@example.com");
        });
    }
}
