package com.todolist.security;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class TokenBlacklist {

    private final Set<String> invalidTokens = ConcurrentHashMap.newKeySet();

    public void addToken(String token) {
        invalidTokens.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return invalidTokens.contains(token);
    }

    public void removeExpiredTokens(Set<String> expiredTokens) {
        invalidTokens.removeAll(expiredTokens);
    }


    public Set<String> getInvalidTokens() {
        return invalidTokens;
    }
}
