package com.morbis.service;

import com.morbis.model.member.entity.Admin;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.team.repository.TeamRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static com.morbis.data.ViewableEntitySource.*;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AdminService.class)
class AdminServiceTest {

    @Autowired
    private AdminService adminService;

    @MockBean private MemberRepository memberRepository;
    @MockBean private TeamRepository teamRepository;
    @MockBean private MemberComplaintRepository memberComplaintRepository;

    @Test
    public void deleteMember(){
        adminService.deleteMember(homePlayer.getId());
        verify(memberRepository, times(1)).deleteById(homePlayer.getId());
    }

    public void deleteTeam(){
        adminService.deleteTeam(home.getId());
        verify(teamRepository, times(1)).deleteById(home.getId());
    }


}