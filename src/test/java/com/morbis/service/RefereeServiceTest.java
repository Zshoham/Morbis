package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import com.morbis.model.game.repository.GameEventRepository;
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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static com.morbis.data.ViewableEntitySource.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RefereeService.class)
public class RefereeServiceTest {

    @Autowired
    private RefereeService refereeService;

    @MockBean private RefereeRepository refereeRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private GameEventRepository gameEventRepository;
    private ArrayList<GameEvent> gameEventList = new ArrayList<>();

    @Before
    public void setUp() {
        initWithID();
        //mock event
        GameEvent gameEvent = new GameEvent(2, GameEventType.GOAL, LocalDateTime.now().minusMinutes(10),5,"game");
        gameEventList.add(gameEvent);
        gameEvent.setGame(game);
        when(refereeRepository.findById(1)).thenReturn(Optional.ofNullable(main));
        when(gameRepository.findById(1)).thenReturn(Optional.ofNullable(game));
    }
    @Test
    public void getRefGamesTest() {
        List<Game> res = refereeService.getRefGames(1);
        assertThat(res.size()==1);
        assertThat(res.get(0).equals(game));
    }
    @Test
    public void getGameTest() {
        Game gameRes = refereeService.getGame(1);
        assertThat(game.equals(gameRes));
    }
    @Test
    public void getGameEventsTest() {
        List<GameEvent> events = refereeService.getGameEvents(1);
        assertThat(events).isEqualTo(game.getEvents());
    }
    @Test
    public void updateGameEventTest() {
        refereeService.updateGameEvent(main.getId(),gameEventList.get(0));
        verify(gameEventRepository).save(gameEventList.get(0));
        gameEventList.get(0).setDate(LocalDateTime.now());
        gameEventList.get(0).getGame().setEndDate(LocalDateTime.now().minusDays(1));
        refereeService.updateGameEvent(main.getId(),gameEventList.get(0));
        verify(gameEventRepository,times(1)).save(any());
    }
    @Test
    public void getOnGoingGameEventsTest() {
        List<GameEvent> events = refereeService.getGameEvents(1);
        assertThat(events).isEqualTo(game.getEvents());
        assertThat(game.getEndDate().isBefore(LocalDateTime.now()));
    }
    @Test
    public void updateOnGoingGameEventTest() {
        refereeService.updateOnGoingGameEvent(main.getId(), gameEventList.get(0));
        verify(gameEventRepository).save(gameEventList.get(0));
        gameEventList.get(0).setDate(LocalDateTime.now());
        gameEventList.get(0).getGame().setEndDate(LocalDateTime.now().minusDays(1));
        refereeService.updateOnGoingGameEvent(main.getId(),gameEventList.get(0));
        // only one invocation that happened in the previous call.
        verify(gameEventRepository,times(1)).save(any());

    }
}
