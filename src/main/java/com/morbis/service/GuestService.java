package com.morbis.service;

import com.morbis.model.member.repository.MemberRepository;

public class GuestService {
    private MemberRepository memberRepository;

    public GuestService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }
}
