package com.morbis.service;

import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.poster.repository.PostRepository;
import com.morbis.model.poster.repository.PosterDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MemberService.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean private GuestService guestService;
    @MockBean private PostRepository postRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private MemberRepository memberRepository;
    @MockBean private PosterDataRepository posterDataRepository;
    @MockBean private MemberComplaintRepository memberComplaintRepository;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void followPage() {
    }

    @Test
    public void followGame() {
    }

    @Test
    public void reportPost() {
    }

    @Test
    public void getSearchHistory() {
    }

    @Test
    public void getMemberInfo() {
    }

    @Test
    public void updateMemberInfo() {
    }

    @Test
    public void searchData() {
    }
}