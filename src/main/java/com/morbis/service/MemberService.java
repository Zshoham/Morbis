package com.morbis.service;

import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.poster.repository.PostRepository;
import com.morbis.model.poster.repository.PosterDataRepository;

public class MemberService {
    private MemberRepository memberRepository;
    private PosterDataRepository posterDataRepository;
    private PostRepository postRepository;
    private MemberComplaintRepository memberComplaintRepository;

    public MemberService(MemberRepository memberRepository, PosterDataRepository posterDataRepository, PostRepository postRepository, MemberComplaintRepository memberComplaintRepository) {
        this.memberRepository = memberRepository;
        this.posterDataRepository = posterDataRepository;
        this.postRepository = postRepository;
        this.memberComplaintRepository = memberComplaintRepository;
    }
}
