package com.morbis.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.morbis.TestUtils;
import com.morbis.api.dto.GameEventDTO;
import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import com.morbis.service.RefereeService;
import com.morbis.service.auth.AuthService;
import com.morbis.service.viewable.MatchReport;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.swing.text.html.Option;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.morbis.TestUtils.listOf;
import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RefereeControllerTest {

    private static final int BAD_ID = 999;

    @Autowired
    private MockMvc apiMock;

    @MockBean
    private AuthService authService;

    @MockBean
    private RefereeService refereeService;

    private GameEvent testEvent;

    private MatchReport testReport;

    @Before
    public void setUp() {
        initWithID();

        testEvent = GameEvent.newGameEvent()
                .type(GameEventType.GOAL)
                .time(LocalDateTime.now(), 55)
                .description("gooooooooooool")
                .withId(888)
                .build();

        game.setEvents(listOf(testEvent));

        testReport = new MatchReport(game);

        when(authService.authorize(any(), any())).thenReturn(true);

        when(refereeService.getRefGames(main.getId())).thenReturn(listOf(game));
        when(refereeService.getGameEvents(game.getId())).thenReturn(listOf(testEvent));
        when(refereeService.getOnGoingGameEvents(main.getId())).thenReturn(listOf(testEvent));
        when(refereeService.updateGameEvent(eq(main.getId()), any(), eq(game.getId()))).thenReturn(true);
        when(refereeService.getMatchReport(main.getId(), game.getId())).thenReturn(testReport);
        when(refereeService.getOngoingGame(main.getId())).thenReturn(Optional.of(game));

        doThrow(new IllegalArgumentException()).when(refereeService).getRefGames(BAD_ID);
        doThrow(new IllegalArgumentException()).when(refereeService).getGameEvents(BAD_ID);
        doThrow(new IllegalArgumentException()).when(refereeService).getOnGoingGameEvents(BAD_ID);
        when(refereeService.updateGameEvent(BAD_ID, testEvent, BAD_ID)).thenReturn(false);
        doThrow(new IllegalStateException()).when(refereeService).getMatchReport(supporting.getId(), game.getId());
        when(refereeService.getOngoingGame(supporting.getId())).thenReturn(Optional.empty());
    }

    @Test
    public void getRefGames() throws UnsupportedEncodingException {
        // positive - correct ref id
        MvcResult response = TestUtils.makeGetRequest("/api/referee/{refID}/games", apiMock, main.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(String.valueOf(game.getId()), Game.getDescription(game)));

        // negative - invalid ref id
        response = TestUtils.makeGetRequest("/api/referee/{refID}/games", apiMock, BAD_ID);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getResponse().getContentAsString())
                .contains("IllegalArgumentException");
    }

    @Test
    public void getOngoingGame() throws UnsupportedEncodingException {
        // positive - ref has an ongoing game
        MvcResult response = TestUtils.makeGetRequest("/api/referee/{refID}/game-ongoing", apiMock, main.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(String.valueOf(game.getId()), Game.getDescription(game)));

        // negative - ref has no ongoing games
        response = TestUtils.makeGetRequest("/api/referee/{refID}/game-ongoing", apiMock, supporting.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getGameEvents() throws UnsupportedEncodingException {
        // positive - correct ref id
        MvcResult response = TestUtils.makeGetRequest("/api/referee/game-events", apiMock,
                listOf(Pair.of("gameID", String.valueOf(game.getId()))));
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(String.valueOf(testEvent.getId()), testEvent.getDescription()));

        // negative - invalid ref id
        response = TestUtils.makeGetRequest("/api/referee/game-events", apiMock,
                listOf(Pair.of("gameID", String.valueOf(BAD_ID))));
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getResponse().getContentAsString())
                .contains("IllegalArgumentException");
    }

    @Test
    public void getOnGoingGameEvents() throws UnsupportedEncodingException {
        // positive - correct ref id
        MvcResult response = TestUtils.makeGetRequest("/api/referee/{refID}/game-events-ongoing",
                apiMock, main.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(String.valueOf(testEvent.getId()), testEvent.getDescription()));

        // negative - invalid ref id
        response = TestUtils.makeGetRequest("/api/referee/{refID}/game-events-ongoing", apiMock, BAD_ID);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getResponse().getContentAsString())
                .contains("IllegalArgumentException");
    }

    @Test
    public void updateGameEvent() {
        GameEventDTO dto = GameEventDTO.fromGameEvent(testEvent);
        // positive - correct ref id
        MvcResult response = TestUtils.makePostRequest("/api/referee/{refID}/update-event",
                apiMock, MediaType.APPLICATION_JSON, dto,
                listOf(Pair.of("gameID", String.valueOf(game.getId()))), main.getId());

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // negative - unauthorized
        response = TestUtils.makePostRequest("/api/referee/{refID}/update-event",
                apiMock, MediaType.APPLICATION_JSON, dto,
                listOf(Pair.of("gameID", String.valueOf(BAD_ID))), BAD_ID);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    public void getMatchReport() throws UnsupportedEncodingException {
        // positive - main referee requests the report.
        MvcResult response = TestUtils.makeGetRequest(
                "/api/referee/{refID}/match-report", apiMock,
                listOf(Pair.of("gameID", String.valueOf(game.getId()))), main.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(Game.getDescription(game), main.getName(), testEvent.getDescription()));

        // negative - supporting referee requests the report.
        response = TestUtils.makeGetRequest(
                "/api/referee/{refID}/match-report", apiMock,
                listOf(Pair.of("gameID", String.valueOf(game.getId()))), supporting.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getResponse().getContentAsString())
                .contains("IllegalStateException");
    }
}