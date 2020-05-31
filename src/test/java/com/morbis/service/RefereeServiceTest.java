package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import com.morbis.model.game.repository.GameEventRepository;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.member.repository.RefereeRepository;
import com.morbis.service.viewable.MatchReport;
import org.junit.Before;
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

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;
import static com.morbis.TestUtils.listOf;


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = RefereeService.class)
public class RefereeServiceTest {

    @Autowired
    private RefereeService refereeService;

    @MockBean private RefereeRepository refereeRepository;
    @MockBean private GameRepository gameRepository;
    @MockBean private GameEventRepository gameEventRepository;
    @MockBean private MemberRepository memberRepository;

    private GameEvent gameEvent;

    @Before
    public void setUp() {
        initWithID();
        //mock event
        gameEvent = new GameEvent(2, GameEventType.GOAL, LocalDateTime.now().minusMinutes(10),5,"game");
        gameEvent.setGame(game);
        game.setEvents(listOf(gameEvent));
        game.setFollowers(listOf(homePlayer, awayPlayer));
        homePlayer.setEventBackLog(new LinkedList<>());
        awayPlayer.setEventBackLog(new LinkedList<>());
        when(refereeRepository.findById(main.getId())).thenReturn(Optional.ofNullable(main));
        when(gameRepository.findById(game.getId())).thenReturn(Optional.ofNullable(game));
    }

    @Test
    public void getRefGamesTest() {
        List<Game> res = refereeService.getRefGames(main.getId());
        assertThat(res.size() == 1);
        assertThat(res).containsExactly(game);
    }

    @Test
    public void getOngoingGame() {
        // positive - ref has an ongoing game
        Optional<Game> ongoing = refereeService.getOngoingGame(main.getId());
        assertThat(ongoing)
                .isPresent()
                .contains(game);

        // negative - ref has no ongoing game
        game.setStartDate(LocalDateTime.now().plusHours(2));
        ongoing = refereeService.getOngoingGame(main.getId());
        assertThat(ongoing).isEmpty();
    }

    @Test
    public void getGameEventsTest() {
        List<GameEvent> events = refereeService.getGameEvents(game.getId());
        assertThat(events).isEqualTo(game.getEvents());
    }

    @Test
    public void updateGameEventTest() {
        game.setEndDate(LocalDateTime.now().minusHours(2));
        refereeService.updateGameEvent(main.getId(), gameEvent, game.getId());
        verify(gameEventRepository).save(gameEvent);
        gameEvent.setDate(LocalDateTime.now());
        gameEvent.getGame().setEndDate(LocalDateTime.now().minusDays(1));
        refereeService.updateGameEvent(main.getId(), gameEvent, game.getId());
        verify(gameEventRepository,times(1)).save(any());
    }

    @Test
    public void getOnGoingGameEventsTest() {
        List<GameEvent> events = refereeService.getOnGoingGameEvents(main.getId());
        assertThat(events).isEqualTo(game.getEvents());
        assertThat(game.getEndDate().isBefore(LocalDateTime.now()));
    }

    @Test
    public void updateOnGoingGameEventTest() {
        refereeService.updateOnGoingGameEvent(main.getId(), gameEvent, game.getId());
        verify(gameEventRepository).save(gameEvent);
        assertThat(homePlayer.getEventBackLog()).containsExactly(gameEvent);
        assertThat(awayPlayer.getEventBackLog()).containsExactly(gameEvent);

        gameEvent.setDate(LocalDateTime.now());
        gameEvent.getGame().setEndDate(LocalDateTime.now().minusDays(1));
        Throwable expectedException = catchThrowable(
                () -> refereeService.updateOnGoingGameEvent(main.getId(), gameEvent, game.getId()));
        assertThat(expectedException).hasMessageContaining("trying to add event after the game is over");
        // only one invocation that happened in the previous call.
        verify(gameEventRepository,times(1)).save(any());
    }

    @Test
    public void getMatchReport() {
        MatchReport report = refereeService.getMatchReport(main.getId(), game.getId());
        assertThat(report.getTitle()).isEqualTo(Game.getDescription(game));
        assertThat(report.getReferee()).isEqualTo(main.getName());
        assertThat(report.getEvents())
                .allMatch(eventStr -> eventStr.contains(gameEvent.getDescription()));
    }
}
