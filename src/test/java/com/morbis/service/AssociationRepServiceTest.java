package com.morbis.service;

import com.morbis.data.ViewableEntitySource;
import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import com.morbis.model.league.entity.Season;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.member.repository.RefereeRepository;
import com.morbis.service.notification.EmailService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.stream.Collectors;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AssociationRepService.class)
public class AssociationRepServiceTest {

    @Autowired
    private AssociationRepService associationRepService;

    @MockBean private SeasonRepository seasonRepository;
    @MockBean private LeagueRepository leagueRepository;
    @MockBean private RefereeRepository refereeRepository;
    @MockBean private EmailService emailService;

    @Before
    public void setUp() {
        ViewableEntitySource.initWithID();
        when(leagueRepository.findAllByName(league.getName())).thenReturn(Collections.singletonList(league));
        when(leagueRepository.findById(league.getId())).thenReturn(Optional.of(league));
        when(seasonRepository.findByLeagueAndYear(league, 2020)).thenReturn(Optional.of(season));
        when(seasonRepository.findById(season.getId())).thenReturn(Optional.of(season));
        when(refereeRepository.save(any(Referee.class))).thenReturn(main);
    }

    @Test
    public void addLeague() {
        // adding league with same name fails.
        assertThat(associationRepService.addLeague(league.getName())).isFalse();

        // adding league with new name succeeds.
        associationRepService.addLeague("new name");
        verify(leagueRepository, times(1)).save(any(League.class));
    }

    @Test
    public void addSeason() {
        // throws when league id is incorrect.
        assertThatThrownBy(() -> associationRepService.addSeason(999, 2000))
                .hasMessageContaining("league");

        // throws when season exists.
        assertThatThrownBy(() -> associationRepService.addSeason(league.getId(), 2020))
                .hasMessageContaining("season");

        // adding correct season succeeds.
        league.setSeasons(new LinkedList<>(league.getSeasons()));
        associationRepService.addSeason(league.getId(), 2021);
        verify(seasonRepository, times(1)).save(any(Season.class));
    }

    @Test
    public void getSeasons() {
        // wrong league id does not return any seasons
        assertThat(associationRepService.getSeasons(999)).isEmpty();

        // correct league succeeds.
        assertThat(associationRepService.getSeasons(league.getId())).isNotEmpty();
    }

    @Test
    public void setScoringMethod() {
        // throws when league id is incorrect.
        assertThatThrownBy(() -> associationRepService.setScoringMethod(999, ScoringMethod.THREE_POINTS_FOR_WIN))
                .hasMessageContaining("league");

        // new method is saved.
        associationRepService.setScoringMethod(league.getId(), ScoringMethod.THREE_POINTS_FOR_WIN);
        verify(leagueRepository, times(1)).save(league);
    }

    @Test
    public void setSchedulingMethod() {
        // throws when league id is incorrect.
        assertThatThrownBy(() -> associationRepService.setSchedulingMethod(999, SchedulingMethod.SINGLE_FIXTURE))
                .hasMessageContaining("league");

        // new method is saved.
        associationRepService.setSchedulingMethod(league.getId(), SchedulingMethod.SINGLE_FIXTURE);
        verify(leagueRepository, times(1)).save(league);
    }

    @Test
    public void addRef() {
        associationRepService.addRef("mail", "name");

        // the new referee is saved.
        verify(refereeRepository, times(1)).save(any(Referee.class));
        
        // an email is sent to the referee.
        verify(emailService, times(1)).registerReferee(any(Referee.class));
    }

    @Test
    public void addRefsToSeason() {
        // throws when season does not exist.
        assertThatThrownBy(() -> associationRepService.addRefsToSeason(999, Collections.singletonList(main)))
                .hasMessageContaining("season");

        // the season is saved
        ArrayList<Season> seasons = new ArrayList<>();
        seasons.add(season);
        ArrayList<Referee> referees = new ArrayList<>();
        referees.add(main);

        main.setSeasons(seasons);
        season.setReferees(referees);
        associationRepService.addRefsToSeason(season.getId(), List.copyOf(referees));
        verify(seasonRepository, times(1)).save(season);
    }

    @Test
    public void removeRefs() {
        ArrayList<Season> seasons = new ArrayList<>();
        seasons.add(season);
        ArrayList<Referee> referees = new ArrayList<>();
        referees.add(main);
        main.setSeasons(seasons);
        season.setReferees(referees);
        List<Integer> refIDs = referees.stream().map(Member::getId).collect(Collectors.toList());
        when(refereeRepository.findAllById(refIDs)).thenReturn(List.copyOf(referees));

        associationRepService.removeRefs(refIDs);

        // referee is removed from season and season is saved.
        verify(seasonRepository, times(1)).save(season);

        // referee is removed.
        verify(refereeRepository, times(1)).delete(main);
    }
}