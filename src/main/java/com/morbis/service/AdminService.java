package com.morbis.service;

import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.team.repository.TeamRepository;

public class AdminService {
    private TeamRepository teamRepository;
    private MemberRepository memberRepository;
    private MemberComplaint memberComplaint;

    public AdminService(TeamRepository teamRepository, MemberRepository memberRepository, MemberComplaint memberComplaint) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.memberComplaint = memberComplaint;
    }
}
