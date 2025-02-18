package com.todolist.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import com.todolist.model.User;
import com.todolist.repositories.UserRepository;
import com.todolist.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SecurityFilterTest {

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();

    }

    @Test
    public void doFilterInternalTest() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        when(request.getHeader("Authorization")).thenReturn("Bearer valid_token");
        when(tokenService.validateToken("valid_token")).thenReturn("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    public void doFilterInternalInvalidTokenTest() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid_token");
        when(tokenService.validateToken("invalid_token")).thenReturn(null); // Assegurar que o token inválido retorna null

        securityFilter.doFilterInternal(request, response, filterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
