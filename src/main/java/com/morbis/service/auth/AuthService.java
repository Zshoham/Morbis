package com.morbis.service.auth;

import com.morbis.MorbisApplication;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import com.morbis.model.member.repository.MemberRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.Optional;


@Service
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;

    private AuthTable auth;

    private final PasswordEncoder passwordEncoder;

    private final Logger logger;


    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        auth = new AuthTable();
        this.logger = LoggerFactory.getLogger(AuthService.class);
        this.passwordEncoder = passwordEncoder;
    }

    protected void cleanAuthTable() {
        logger.trace("called function: AuthService->cleanAuthTable.");
        auth = new AuthTable();
    }


    public void register(Member member) {
        logger.trace("called function: AuthService->register.");
        if (memberRepository.findDistinctByUsername(member.getUsername()).isPresent()) {
            logger.info("the userName: " + member.getUsername() + " is already used.");
            throw new IllegalArgumentException("user already registered");
        }

        String hashedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(hashedPassword);

        memberRepository.save(member);
        logger.info("member with the username: " + member.getUsername() + " has been registered.");
    }


        public Optional<Pair<Member, String>> login(String username, String password) {
        logger.trace("called function: AuthService->login.");
        Optional<Member> user = memberRepository.findDistinctByUsername(username);
        if (user.isEmpty()) {
            logger.info("invalid login information, username -  " + username + " does not exist");
            return Optional.empty();
        }

        if (!passwordEncoder.matches(password, user.get().getPassword())) {
            logger.info("invalid login information, password is incorrect");
            return Optional.empty();
        }

        logger.info("the member " + username + " has successfully logged in.");

        return Optional.of(Pair.of(user.get(), auth.createToken(user.get())));
    }

    public void logout(String token) {
        logger.trace("called function: AuthService->logout.");
        if (MorbisApplication.DEV_MODE && token.equals("42"))
            auth = new AuthTable();
        auth.removeToken(token);
    }

    public boolean authorize(String token, MemberRole role) {
        logger.trace("called function: AuthService->authorize.");
        Optional<Integer> authority = auth.validate(token);

        // the token is invalid.
        if (authority.isEmpty()) {
            logger.info("token is invalid");
            return false;
        }

        Optional<Member> actualMember = memberRepository.findById(authority.get());

        // the logged in member does not exist in the database.
        if (actualMember.isEmpty()) {
            logger.error("the member owning the token does not exist in the database (id=" + authority.get());
            return false;
        }

        // return true if the requested role is the same as the accrual role.
        // if the requested role is of a fan, all others roles would also get authorization.
        return actualMember.get().getMemberRole().contains(role) || role == MemberRole.FAN;
    }
}
