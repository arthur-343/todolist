package com.todolist.security;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenCleanupTask {

    private final TokenBlacklist tokenBlacklist;
    private final TokenService tokenService;

    @Scheduled(fixedRate = 3600000) // Executa a cada 1 hora
    public void cleanupExpiredTokens() {
        Set<String> expiredTokens = tokenBlacklist.getInvalidTokens().stream()
                .filter(tokenService::isTokenExpired) // Implementar lógica de expiração no TokenService
                .collect(Collectors.toSet());

        tokenBlacklist.removeExpiredTokens(expiredTokens);
    }
}
