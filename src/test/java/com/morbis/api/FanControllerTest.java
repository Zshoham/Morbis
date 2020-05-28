package com.morbis.api;

import com.morbis.TestUtils;
import com.morbis.api.dto.CoachRegistrationDTO;
import com.morbis.api.dto.PlayerRegistrationDTO;
import com.morbis.model.game.entity.Game;
import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.service.MemberService;
import com.morbis.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static com.morbis.TestUtils.listOf;
import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class FanControllerTest {

    @Autowired
    private MockMvc apiMock;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        initWithID();

        testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .withId(1)
                .build();

        when(authService.authorize(any(), any())).thenReturn(true);
    }

    @Test
    void registerAsCoach() {
        String qualification = "qualifications";
        String role = "role";

        CoachRegistrationDTO coachDTO = new CoachRegistrationDTO(qualification, role);

        // positive test
        MvcResult response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/registerAsCoach", apiMock, MediaType.APPLICATION_JSON, coachDTO, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // negative test
        doThrow(new IllegalArgumentException()).when(memberService).
                registerAsCoach(testMember.getId(), qualification, role);
        response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/registerAsCoach", apiMock, MediaType.APPLICATION_JSON, coachDTO, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void registerAsPlayer() {
        LocalDate birthday = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strBirthday = birthday.format(formatter);
        String position = "Goalkeeper";
        PlayerRegistrationDTO playerDTO = new PlayerRegistrationDTO(strBirthday, position);

        // positive test
        MvcResult response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/registerAsPlayer", apiMock, MediaType.APPLICATION_JSON, playerDTO, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // negative test
        doThrow(new IllegalArgumentException()).when(memberService).
                registerAsPlayer(testMember.getId(), birthday, position);
        response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/registerAsPlayer", apiMock, MediaType.APPLICATION_JSON, playerDTO, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void requestRegisterAsTeamOwner() {
        // positive test
        String newTeamName = "new team";
        MvcResult response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/requestRegisterAsTeamOwner", apiMock, MediaType.APPLICATION_JSON, newTeamName, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // negative test
        doThrow(new IllegalArgumentException()).when(memberService).
                requestRegisterAsTeamOwner(testMember.getId(), newTeamName);
        response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/requestRegisterAsTeamOwner", apiMock, MediaType.APPLICATION_JSON, newTeamName, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void followGame() {
        // positive test
        int gameId = 1;
        MvcResult response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/followGame", apiMock, MediaType.APPLICATION_JSON,
                List.of(Pair.of("gameID", String.valueOf(gameId))), testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // negative test
        doThrow(new IllegalArgumentException()).when(memberService).
                followGame(testMember.getId(), gameId);
        response = TestUtils.makePostRequest(
                "/api/fan/{memberID}/followGame", apiMock, MediaType.APPLICATION_JSON,
                List.of(Pair.of("gameID", String.valueOf(gameId))), testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getGamesFollowing() throws UnsupportedEncodingException {
        when(memberService.getGamesFollowing(testMember.getId())).thenReturn(listOf(game));
        when(memberService.getGamesFollowing(awayManager.getId())).thenReturn(Collections.emptyList());

        // has games
        MvcResult response = TestUtils.makeGetRequest(
                "/api/fan/{memberID}/gamesFollowing", apiMock, testMember.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(String.valueOf(game.getId()), Game.getDescription(game)));

        // has no games
        response = TestUtils.makeGetRequest(
                "/api/fan/{memberID}/gamesFollowing", apiMock, awayManager.getId());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());

    }
}