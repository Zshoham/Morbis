package com.morbis.service.auth;

import com.morbis.model.member.entity.Member;

import java.security.MessageDigest;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

class AuthTable {

    private ConcurrentHashMap<String, Integer> authTokens;

    public AuthTable() {
        authTokens = new ConcurrentHashMap<>();
    }

    public String createToken(Member member) {

        long salt = Double.doubleToLongBits(Math.random());

        String input = member.getEmail() + member.getName()
                + member.getUsername() + member.getMemberRole()
                + salt;

        String token = "e2f6c302d6af17940110dff226b9bf458601370b";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-256");
            crypt.reset();
            crypt.update(input.getBytes());
            token = new String(crypt.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }

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
