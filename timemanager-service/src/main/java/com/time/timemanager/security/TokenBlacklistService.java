package com.time.timemanager.security;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlacklistService {
    private final Set<String> blacklistedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        this.blacklistedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return this.blacklistedTokens.contains(token);
    }
}