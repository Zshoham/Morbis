package com.morbis.service.auth;

import com.morbis.model.member.entity.Member;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

class AuthTable {

    private final ConcurrentHashMap<String, Integer> authTokens;

    private final HashSet<Integer> userPool;

    private final Timer logoutScheduler;


    public AuthTable() {
        this.userPool = new HashSet<>();
        authTokens = new ConcurrentHashMap<>();
        logoutScheduler = new Timer();
    }

    public String createToken(Member member) {
        if (userPool.contains(member.getId()))
            throw new IllegalArgumentException("user is already logged in");

        SecureRandom generator = new SecureRandom();
        byte[] tokenBytes = new byte[64];
        generator.nextBytes(tokenBytes);
        String token = new HexBinaryAdapter().marshal(tokenBytes);

        Integer id = member.getId();
        authTokens.put(token, id);
        userPool.add(id);

        // token will expire after 15 minutes
        logoutScheduler.schedule(new TimerTask() {
            @Override
            public void run() {
                removeToken(token);
            }
        }, 900000);

        return token;
    }

    public Optional<Integer> validate(String token) {
        return Optional.ofNullable(authTokens.get(token));
    }

    public void removeToken(String token) {
        if (!authTokens.containsKey(token))
            return;

        userPool.remove(authTokens.get(token));
        authTokens.remove(token);
    }

}
