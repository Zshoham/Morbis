package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import com.morbis.model.game.repository.GameEventRepository;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.repository.RefereeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RefereeService.class)
public class RefereeServiceTest {

    @Autowired
    private RefereeService refereeService;

    @MockBean private RefereeRepository refereeRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private GameEventRepository gameEventRepository;

    private GameEvent gameEvent;

    @Before
    public void setUp() {
        initWithID();
        //mock event
        gameEvent = new GameEvent(2, GameEventType.GOAL, LocalDateTime.now().minusMinutes(10),5,"game");
        gameEvent.setGame(game);
        when(refereeRepository.findById(main.getId())).thenReturn(Optional.ofNullable(main));
        when(gameRepository.findById(game.getId())).thenReturn(Optional.ofNullable(game));
    }

    @Test
    public void getRefGamesTest() {
        List<Game> res = refereeService.getRefGames(main.getId());
        assertThat(res.size() == 1);
        assertThat(res.get(0).equals(game));
    }

    @Test
    public void getGameTest() {
        Game gameRes = refereeService.getGame(game.getId());
        assertThat(game.equals(gameRes));
    }

    @Test
    public void getGameEventsTest() {
        List<GameEvent> events = refereeService.getGameEvents(game.getId());
        assertThat(events).isEqualTo(game.getEvents());
    }

    @Test
    public void updateGameEventTest() {
        refereeService.updateGameEvent(main.getId(), gameEvent, game.getId());
        verify(gameEventRepository).save(gameEvent);
        gameEvent.setDate(LocalDateTime.now());
        gameEvent.getGame().setEndDate(LocalDateTime.now().minusDays(1));
        refereeService.updateGameEvent(main.getId(), gameEvent, game.getId());
        verify(gameEventRepository,times(1)).save(any());
    }

    @Test
    public void getOnGoingGameEventsTest() {
        List<GameEvent> events = refereeService.getGameEvents(game.getId());
        assertThat(events).isEqualTo(game.getEvents());
        assertThat(game.getEndDate().isBefore(LocalDateTime.now()));
    }

    @Test
    public void updateOnGoingGameEventTest() {
        refereeService.updateOnGoingGameEvent(main.getId(), gameEvent, game.getId());
        verify(gameEventRepository).save(gameEvent);
        gameEvent.setDate(LocalDateTime.now());
        gameEvent.getGame().setEndDate(LocalDateTime.now().minusDays(1));
        refereeService.updateOnGoingGameEvent(main.getId(), gameEvent, game.getId());
        // only one invocation that happened in the previous call.
        verify(gameEventRepository,times(1)).save(any());
    }
}
