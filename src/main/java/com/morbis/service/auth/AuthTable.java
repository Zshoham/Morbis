package com.morbis.service.auth;

import com.morbis.model.member.entity.Member;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AuthTable {

    private ConcurrentHashMap<String, Integer> authTokens;

    public AuthTable() {
        authTokens = new ConcurrentHashMap<>();
    }

    public String createToken(Member member) {
        SecureRandom generator = new SecureRandom();
        byte[] tokenBytes = new byte[512];
        generator.nextBytes(tokenBytes);
        String token = new String(tokenBytes);
        authTokens.put(token, member.getId());
        return token;
    }

    public Optional<Integer> validate(String token) {
        return Optional.ofNullable(authTokens.get(token));
    }

    public void removeToken(String token) {
        authTokens.remove(token);
    }

}
