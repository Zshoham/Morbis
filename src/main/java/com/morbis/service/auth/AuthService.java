package com.morbis.service.auth;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import com.morbis.model.member.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthService {

    private final MemberRepository memberRepository;

    private final AuthTable auth;


    public AuthService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        auth = new AuthTable();
    }

    public Optional<String> login(String username, String password) {
        Optional<Member> user = memberRepository.findDistinctByUsername(username);
        if (user.isEmpty())
            return Optional.empty();

        if (!password.equals(user.get().getPassword()))
            return Optional.empty();

        return Optional.of(auth.createToken(user.get()));
    }

    public void logout(String token) {
        auth.removeToken(token);
    }

    public boolean authorize(String token, MemberRole role) {
        Optional<Integer> authority = auth.validate(token);

        // the token is invalid.
        if (authority.isEmpty())
            return false;

        Optional<Member> accrualMember = memberRepository.findById(authority.get());

        // the logged in member does not exist in the database.
        if (accrualMember.isEmpty())
            return false;

        // return true if the requested role is the same as the accrual role.
        // if the requested role is of a fan, all others roles would also get authorization.
        return accrualMember.get().getMemberRole() == role || role == MemberRole.FAN;
    }
}
