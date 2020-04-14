package com.morbis.service;

import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.model.team.repository.TransactionRepository;
import com.morbis.service.viewable.Asset;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TeamOwnerService.class)
public class TeamOwnerServiceTest {

    @Autowired
    private TeamOwnerService teamOwnerService;

    @MockBean private MemberRepository memberRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private LeagueRepository leagueRepository;
    @MockBean private SeasonRepository seasonRepository;
    @MockBean private CoachRepository coachRepository;
    @MockBean private PlayerRepository playerRepository;
    @MockBean private RefereeRepository refereeRepository;
    @MockBean private TeamManagerRepository teamManagerRepository;
    @MockBean private StadiumRepository stadiumRepository;
    @MockBean private TeamRepository teamRepository;
    @MockBean private TeamOwnerRepository teamOwnerRepository;
    @MockBean private TransactionRepository transactionRepository;

    @Test
    public void getTeamlessAssets() {
                            // setup mock
        // initiallize player
        LinkedList<Player> teamlessPlayers = new LinkedList<>();
        Player testPlayer = new Player(1, "playerUser", "playerPass", "playerName", "playerEmail",LocalDateTime.now(),"Goal Keeper");
        teamlessPlayers.add(testPlayer);
        when(playerRepository.findAllByTeamIsNull()).thenReturn(teamlessPlayers);
        // initiallize stadium
        LinkedList<Stadium> teamlessStadiums = new LinkedList<>();
        Stadium testStadium = new Stadium(2, "Sami Ofer");
        teamlessStadiums.add(testStadium);
        when(stadiumRepository.findAllByTeamIsNull()).thenReturn(teamlessStadiums);
        // initiallize coach
        LinkedList<Coach> teamlessCoaches = new LinkedList<>();
        Coach testCoach = new Coach(3,"coachUser","coachPass" ,"coachName" ,"coachEmail" ,"Qualifications are for losers" ,"Manager Assistant");
        teamlessCoaches.add(testCoach);
        when(coachRepository.findAllByTeamIsNull()).thenReturn(teamlessCoaches);

        // test assets don't have teams
        List<Asset> teamlessAssets = teamOwnerService.getTeamlessAssets();
        // verifies that every asset doesn't have a team
        assertThat(teamlessAssets)
                .hasSize(3);

        // test all assets have teams
        Team testTeam = new Team();
        testCoach.setTeam(testTeam);
        testStadium.setTeam(testTeam);
        testPlayer.setTeam(testTeam);
        teamlessAssets = teamOwnerService.getTeamlessAssets();
        assertThat(teamlessAssets)
                .hasSize(0);
    }


}
