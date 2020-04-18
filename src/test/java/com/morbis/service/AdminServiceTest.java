package com.morbis.service;

import com.morbis.data.ViewableEntitySource;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.poster.entity.Post;
import com.morbis.model.team.entity.TeamStatus;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.service.notification.EmailService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.swing.text.html.Option;

import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static com.morbis.data.ViewableEntitySource.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AdminService.class)
public class AdminServiceTest {



    @Autowired
    private AdminService adminService;

    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private TeamRepository teamRepository;
    @MockBean
    private MemberComplaintRepository memberComplaintRepository;
    @MockBean
    private EmailService emailService;
    @MockBean
    private Logger logger;

    @Before
    public void setUp() {
        ViewableEntitySource.initWithID();
        when(teamRepository.findById(home.getId())).thenReturn(Optional.of(home));
        when(memberRepository.findById(homePlayer.getId())).thenReturn(Optional.of(homePlayer));
    }

    @Test
    public void deleteMember() {
        adminService.deleteMember(homePlayer.getId());
        verify(memberRepository, times(1)).deleteById(homePlayer.getId());
    }

    @Test
    public void deleteTeam() throws MessagingException {
        System.out.println(System.getProperty("user.dir"));
        adminService.deleteTeam(home.getId());
        assertThat(home.getTeamStatus()).isEqualByComparingTo(TeamStatus.PERMANENTLY_CLOSED);
        verify(emailService, times(1)).closeTeam(home);
    }

    @Test
    public void sendFeedback() throws MessagingException {
        Post post = new Post(1,"test","test123");
        MemberComplaint memberComplaint = new MemberComplaint(178495,homePlayer,post,"complaintDescription-test");
        adminService.sendFeedback(homePlayer.getId(),"bla bla bla",memberComplaint);
        verify(emailService,times(1)).sendMessage(any(),any(),any());
    }


}