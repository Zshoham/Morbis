package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.poster.repository.PostRepository;
import com.morbis.model.poster.repository.PosterDataRepository;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.service.viewable.ViewableEntityType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.morbis.data.MemberServiceDataSource.*;
import static com.morbis.TestUtils.listOf;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = MemberService.class)
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean private GuestService guestService;
    @MockBean private TeamRepository teamRepository;
    @MockBean private PostRepository postRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private CoachRepository coachRepository;
    @MockBean private MemberRepository memberRepository;
    @MockBean private PlayerRepository playerRepository;
    @MockBean private TeamOwnerRepository teamOwnerRepository;
    @MockBean private PosterDataRepository posterDataRepository;
    @MockBean private MemberComplaintRepository memberComplaintRepository;
    @MockBean private TeamOwnerRegRequestRepository teamOwnerRegRequestRepository;


    @Before
    public void setUp() {
        initWithID();
        homePlayer.setSearches(new LinkedList<>());
        homePlayer.setPagesFollowing(new LinkedList<>());
        when(memberRepository.findById(homePlayer.getId()))
                .thenReturn(Optional.ofNullable(homePlayer));
        when(memberRepository.findById(homeOwner.getId()))
                .thenReturn(Optional.ofNullable(homeOwner));
        when(memberRepository.findById(simpleMember.getId()))
                .thenReturn(Optional.ofNullable(simpleMember));
        when(teamRepository.findAllByNameContaining(home.getName()))
                .thenReturn(List.of(home));
    }


    private void setupFollowPageMock() {
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


    private void setupFollowGameMock() {
        game.setFollowers(new ArrayList<>());
        homePlayer.setGamesFollowing(new ArrayList<>());
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
        assertThat(homePlayer.getGamesFollowing()).contains(game);

        // non-existing member follow exist game
        assertThatThrownBy(() -> memberService.followGame(999, game.getId()))
                .hasMessageContaining("Invalid member id");

        // exist member follow non-existing game
        assertThatThrownBy(() -> memberService.followGame(homePlayer.getId(), 999))
                .hasMessageContaining("Invalid game id");
    }

    private void setupGetGameMock() {
        homePlayer.setGamesFollowing(listOf(game));
        when(memberRepository.findById(homePlayer.getId()))
                .thenReturn(Optional.ofNullable(homePlayer));
        when(memberRepository.findById(awayPlayer.getId()))
                .thenReturn(Optional.ofNullable(awayPlayer));
    }

    @Test
    public void getGamesFollowing() {
        setupGetGameMock();

        List<Game> following = memberService.getGamesFollowing(homePlayer.getId());
        assertThat(following).containsExactly(game);

        following = memberService.getGamesFollowing(awayPlayer.getId());
        assertThat(following).isEmpty();
    }

    private void setupReportPostMock() {
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

    @Test
    public void registerAsCoach() {
        //exist Member
        memberService.registerAsCoach(simpleMember.getId(), "qualifications", "role");
        verify(memberRepository).save(simpleMember);
        verify(coachRepository).save(any(Coach.class));

        //member that is already a Coach
        assertThatThrownBy(() -> memberService.registerAsCoach(simpleMember.getId(), "qualifications", "role"))
                .hasMessageContaining("The member is already a coach");

        //non exist member
        assertThatThrownBy(() -> memberService.registerAsCoach(999, "qualif", "role"))
                .hasMessageContaining("Invalid member id");
    }

    @Test
    public void registerAsPlayer() {
        //exist Member
        LocalDate birthday = LocalDate.now();
        memberService.registerAsPlayer(simpleMember.getId(), birthday, "CM");
        verify(memberRepository).save(simpleMember);
        verify(playerRepository).save(any(Player.class));

        //member that is already a Player
        assertThatThrownBy(() -> memberService.registerAsPlayer(simpleMember.getId(), birthday, "CM"))
                .hasMessageContaining("The member is already a player");

        //non exist member
        assertThatThrownBy(() -> memberService.registerAsPlayer(999, birthday,"CM"))
                .hasMessageContaining("Invalid member id");
    }

    @Test
    public void validateRegisterAsTeamOwner(){
        // positive test
            // exist Member, non exist team
        Throwable possibleException = catchThrowable(() -> memberService.validateRegisterAsTeamOwner(simpleMember.getId(), "new team"));
        assertThat(possibleException).doesNotThrowAnyException();

        // negative tests
            // member that is already a TeamOwner
        assertThatThrownBy(() -> memberService.validateRegisterAsTeamOwner(homeOwner.getId(), "new team"))
                .hasMessageContaining("The member is already a team owner");
            // non exist member
        assertThatThrownBy(() -> memberService.validateRegisterAsTeamOwner(999, "new team"))
                .hasMessageContaining("Invalid member id");
            // exist team with same name
        assertThatThrownBy(() -> memberService.validateRegisterAsTeamOwner(homePlayer.getId(), home.getName()))
                .hasMessageContaining("team already exist");
    }

    @Test
    public void requestRegisterAsTeamOwner(){
        // positive tests
            // no requests pending
        Throwable possibleException = catchThrowable(() -> memberService.validateRegisterAsTeamOwner(simpleMember.getId(), "new team"));
        assertThat(possibleException).doesNotThrowAnyException();

        // negative tests
            // request pending
        when(teamOwnerRegRequestRepository.findById(simpleMember.getId()))
                .thenReturn(Optional.of(new TeamOwnerRegRequest(simpleMember)));
        assertThatThrownBy(() -> memberService.requestRegisterAsTeamOwner(simpleMember.getId()))
                .hasMessageContaining("member already requested to assign as team owner- request pending");
    }

    @Test
    public void registerAsTeamOwner() {
        memberService.registerAsTeamOwner(simpleMember.getId());
        verify(memberRepository).save(simpleMember);
        verify(teamOwnerRepository, times(1)).save(any(TeamOwner.class));
    }
}