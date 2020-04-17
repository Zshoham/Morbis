package com.morbis.service;

import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.member.entity.MemberSearch;
import com.morbis.model.member.entity.Player;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.poster.repository.PostRepository;
import com.morbis.model.poster.repository.PosterDataRepository;
import com.morbis.service.viewable.ViewableEntityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.morbis.data.MemberServiceDataSource.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        initWithID();
        homePlayer.setSearches(new LinkedList<>());
        homePlayer.setPagesFollowing(new LinkedList<>());
        when(memberRepository.findById(homePlayer.getId()))
                .thenReturn(Optional.ofNullable(homePlayer));
    }

    private void setupFollowPageMock(){
        when(posterDataRepository.findById(posterData.getId()))
                .thenReturn(Optional.ofNullable(posterData));
    }
    @Test
    public void followPage() {
        setupFollowPageMock();

        // exist member follow exist page
        Throwable goodFollow = catchThrowable(() -> memberService.followPage(homePlayer.getId(), posterData.getId()));
        assertThat(goodFollow).doesNotThrowAnyException();
        assertThat(posterData.getFollowers()).contains(homePlayer);
        assertThat(homePlayer.getPagesFollowing()).contains(posterData);

        // non-existing member follow exist page
        assertThatThrownBy(() -> memberService.followPage(999, posterData.getId()))
                .hasMessageContaining("Invalid member id");

        // exist member follow non-existing page
        assertThatThrownBy(() -> memberService.followPage(homePlayer.getId(), 999))
                .hasMessageContaining("Invalid page id");
    }


    private void setupFollowGameMock(){
        game.setFollowers(new LinkedList<>());
        when(gameRepository.findById(game.getId()))
                .thenReturn(Optional.ofNullable(game));
    }
    @Test
    public void followGame() {
        setupFollowGameMock();

        // exist member follow exist game
        Throwable goodFollow = catchThrowable(() -> memberService.followGame(homePlayer.getId(), game.getId()));
        assertThat(goodFollow).doesNotThrowAnyException();
        assertThat(game.getFollowers()).contains(homePlayer);

        // non-existing member follow exist game
        assertThatThrownBy(() -> memberService.followGame(999, game.getId()))
                .hasMessageContaining("Invalid member id");

        // exist member follow non-existing game
        assertThatThrownBy(() -> memberService.followGame(homePlayer.getId(), 999))
                .hasMessageContaining("Invalid game id");
    }

    private void setupReportPostMock(){
        when(postRepository.findById(post.getId()))
                .thenReturn(Optional.ofNullable(post));
    }
    @Test
    public void reportPost() {
        setupReportPostMock();

        // exist member follow exist game
        Throwable goodReport = catchThrowable(() -> memberService.reportPost(homePlayer.getId(), post.getId(), ""));
        assertThat(goodReport).doesNotThrowAnyException();
        verify(memberComplaintRepository).save(any(MemberComplaint.class));

        // non-existing member follow exist game
        assertThatThrownBy(() -> memberService.reportPost(999, post.getId(), ""))
                .hasMessageContaining("Invalid member id");

        // exist member follow non-existing game
        assertThatThrownBy(() -> memberService.reportPost(homePlayer.getId(), 999, ""))
                .hasMessageContaining("Invalid post id");
    }

    @Test
    public void getSearchHistory() {
        // non-existing member
        assertThatThrownBy(() -> memberService.getSearchHistory(999))
                .hasMessageContaining("Invalid member id");

        // exist member
        List<MemberSearch> history = memberService.getSearchHistory(homePlayer.getId());
        assertThat(history).hasSize(0);
        memberService.searchData(ViewableEntityType.all, "", homePlayer.getId());
        history = memberService.getSearchHistory(homePlayer.getId());
        assertThat(history).hasSize(1);
    }

    @Test
    public void getMemberInfo() {
        // non-existing member
        assertThatThrownBy(() -> memberService.getMemberInfo(999))
                .hasMessageContaining("Invalid member id");

        // exist member
        Member member = memberService.getMemberInfo(homePlayer.getId());
        assertThat(member).isEqualTo(homePlayer);
    }

    @Test
    public void updateMemberInfo() {
        // non-existing member
        Player fictionalPlayer = awayPlayer;
        assertThatThrownBy(() -> memberService.updateMemberInfo(fictionalPlayer))
                .hasMessageContaining("Invalid member id");

        // exist member
        Player updatedHomePlayer = homePlayer;
        memberService.updateMemberInfo(updatedHomePlayer);
        verify(memberRepository).save(updatedHomePlayer);
    }

    @Test
    public void searchData() {
        // non-existing member
        assertThatThrownBy(() -> memberService.searchData(ViewableEntityType.all, "", 999))
                .hasMessageContaining("Invalid member id");

        // exist member
        memberService.searchData(ViewableEntityType.all, "", homePlayer.getId());
        verify(memberRepository).save(homePlayer);
    }
}