package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.repository.*;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.service.viewable.SearchResult;
import com.morbis.service.viewable.ViewableEntityType;
import com.morbis.service.viewable.ViewableProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static com.morbis.data.ViewableEntitySource.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = GuestService.class)
public class GuestServiceTest {

    @Autowired
    private GuestService guestService;

    @MockBean private MemberRepository memberRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private LeagueRepository leagueRepository;
    @MockBean private SeasonRepository seasonRepository;
    @MockBean private CoachRepository coachRepository;
    @MockBean private PlayerRepository playerRepository;
    @MockBean private RefereeRepository refereeRepository;
    @MockBean private TeamOwnerRepository teamOwnerRepository;
    @MockBean private TeamManagerRepository teamManagerRepository;
    @MockBean private StadiumRepository stadiumRepository;
    @MockBean private TeamRepository teamRepository;




    private void setUpSearchMock() {
        initWithID();

        // setup mock
        when(stadiumRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(homeStadium, awayStadium).collect(Collectors.toList()));

        when(playerRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(homePlayer, awayPlayer).collect(Collectors.toList()));

        when(teamOwnerRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(homeOwner, awayOwner).collect(Collectors.toList()));

        when(teamManagerRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(homeManager, awayManager).collect(Collectors.toList()));

        when(coachRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(homeCoach, awayCoach).collect(Collectors.toList()));

        when(teamRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(home, away).collect(Collectors.toList()));

        when(refereeRepository.findAllByNameContaining("name"))
                .thenReturn(Stream.of(main, supporting).collect(Collectors.toList()));

        when(gameRepository.findAllContainingQuery("name"))
                .thenReturn(Collections.singletonList(game));

        when(seasonRepository.findAllByYearContaining(2020))
                .thenReturn(Collections.singletonList(season));

        when(leagueRepository.findAllByNameContaining("name"))
                .thenReturn(Collections.singletonList(league));
    }

    @Test
    public void searchData() {
        setUpSearchMock();

        // search all
        Collection<SearchResult> allResult = guestService.searchData(ViewableEntityType.all, "name");
        assertThat(allResult)
                .hasSize(16)
                .doesNotContain(new SearchResult(season.getId(), String.valueOf(season.getYear()), ViewableEntityType.SEASON))
                .extracting("id")
                        .containsAll(IntStream.range(1, 17).boxed()
                                .filter(i -> i != season.getId()).collect(Collectors.toList())); // all but season id

        // search all with for season
        Collection<SearchResult> seasonResult = guestService.searchData(ViewableEntityType.all, "2020");
        assertThat(seasonResult)
                .hasSize(1)
                .containsExactly(new SearchResult(season.getId(), String.valueOf(season.getYear()), ViewableEntityType.SEASON));

        // search assets
        Collection<SearchResult> assetsResult = guestService.searchData(ViewableEntityType.assets, "name");
        assertThat(assetsResult)
                .hasSize(10)
                .extracting("id").containsOnly(homePlayer.getId(), awayPlayer.getId(),
                                                        homeCoach.getId(), awayCoach.getId(),
                                                        homeOwner.getId(), awayOwner.getId(),
                                                        homeManager.getId(), awayManager.getId(),
                                                        homeStadium.getId(), awayStadium.getId());

        // search pages
        Collection<SearchResult> pagesResult = guestService.searchData(ViewableEntityType.pages, "name");
        assertThat(pagesResult)
                .hasSize(6)
                .extracting("id").containsOnly(homePlayer.getId(), awayPlayer.getId(),
                                                        homeCoach.getId(), awayCoach.getId(),
                                                        home.getId(), away.getId());

        // search game
        Collection<SearchResult> gameResult = guestService.searchData(Collections.singletonList(ViewableEntityType.GAME), "name");
        assertThat(gameResult)
                .hasSize(1)
                .containsExactly(new SearchResult(game.getId(),
                        Game.getDescription(game),
                        ViewableEntityType.GAME));

    }

    private void setUpGetMock() {
        setUpSearchMock();

        // setup mock
        when(stadiumRepository.findById(homeStadium.getId()))
                .thenReturn(Optional.of(homeStadium));

        when(stadiumRepository.findById(awayStadium.getId()))
                .thenReturn(Optional.of(awayStadium));

        when(playerRepository.findById(homePlayer.getId()))
                .thenReturn(Optional.of(homePlayer));

        when(playerRepository.findById(awayPlayer.getId()))
                .thenReturn(Optional.of(awayPlayer));

        when(teamOwnerRepository.findById(homeOwner.getId()))
                .thenReturn(Optional.of(homeOwner));

        when(teamOwnerRepository.findById(awayOwner.getId()))
                .thenReturn(Optional.of(awayOwner));

        when(teamManagerRepository.findById(homeManager.getId()))
                .thenReturn(Optional.of(homeManager));

        when(teamManagerRepository.findById(awayManager.getId()))
                .thenReturn(Optional.of(awayManager));

        when(coachRepository.findById(homeCoach.getId()))
                .thenReturn(Optional.of(homeCoach));

        when(coachRepository.findById(awayCoach.getId()))
                .thenReturn(Optional.of(awayCoach));

        when(teamRepository.findById(home.getId()))
                .thenReturn(Optional.of(home));

        when(teamRepository.findById(away.getId()))
                .thenReturn(Optional.of(away));

        when(refereeRepository.findById(main.getId()))
                .thenReturn(Optional.of(main));

        when(refereeRepository.findById(supporting.getId()))
                .thenReturn(Optional.of(supporting));

        when(gameRepository.findById(game.getId()))
                .thenReturn(Optional.of(game));

        when(seasonRepository.findById(season.getId()))
                .thenReturn(Optional.of(season));

        when(leagueRepository.findById(league.getId()))
                .thenReturn(Optional.of(league));
    }


    @Test
    public void getData() {
        setUpGetMock();

        Collection<SearchResult> all = guestService.searchData(ViewableEntityType.all, "name");
        all.addAll(guestService.searchData(Collections.singletonList(ViewableEntityType.SEASON), "2020"));

        List<ViewableProperties<?>> properties = all.stream().map(guestService::getData).collect(Collectors.toList());
        List<ViewableProperties<?>> actualProperties =
                Stream.of(homeStadium, awayStadium, homePlayer, awayPlayer,
                        homeOwner, awayOwner, homeManager, awayManager,
                        homeCoach, awayCoach, home, away,
                        main, supporting, game, season, league)
                .map(ViewableProperties::from)
                .collect(Collectors.toList());

        assertThat(properties).containsAll(actualProperties);
    }
}