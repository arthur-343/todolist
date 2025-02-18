package com.todolist.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.todolist.model.User;
import com.todolist.repositories.UserRepository;
import com.todolist.service.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);
        System.out.println("🔍 Token recebido: " + token);

        if (token != null && !token.isEmpty()) {
            String login = tokenService.validateToken(token);
            System.out.println("🔍 Login associado ao token: " + login);

            if (login != null) {
                User user = userRepository.findByEmail(login);
                if (user != null) {
                    var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, authorities));
                    System.out.println("✅ Autenticação bem-sucedida para: " + login);
                } else {
                    System.out.println("❌ Usuário não encontrado para o e-mail: " + login);
                }
            } else {
                System.out.println("❌ Token inválido ou expirado.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            System.out.println("⚠️ Nenhum token encontrado no cabeçalho Authorization.");
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}
