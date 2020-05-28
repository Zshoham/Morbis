package com.morbis.api;

import com.morbis.api.dto.LeagueDTO;
import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import com.morbis.model.member.entity.TeamOwnerRegRequest;
import com.morbis.service.AssociationRepService;
import com.morbis.service.MemberService;
import com.morbis.service.auth.AuthService;
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

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.morbis.TestUtils.*;
import static com.morbis.data.MemberServiceDataSource.initWithID;
import static com.morbis.data.MemberServiceDataSource.simpleMember;
import static com.morbis.data.ViewableEntitySource.homePlayer;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AssociationRepControllerTest {

    @Autowired
    private MockMvc apiMock;

    @MockBean
    private AssociationRepService associationRepService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AuthService authService;

    private League testLeague;

    @Before
    public void setUp() {
        initWithID();
        testLeague = League.newLeague("La liga").build();

        when(authService.authorize(any(), any())).thenReturn(true);

        when(associationRepService.getLeagues()).thenReturn(listOf(testLeague));
        when(associationRepService.getScoringMethods())
                .thenReturn(Stream.of(ScoringMethod.values()).collect(Collectors.toList()));
        when(associationRepService.getSchedulingMethods())
                .thenReturn(Stream.of(SchedulingMethod.values()).collect(Collectors.toList()));
    }

    @Test
    public void getLeagues() throws UnsupportedEncodingException {
        MvcResult response = makeGetRequest("/api/association-rep/leagues", apiMock);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(
                        testLeague.getName(),
                        String.valueOf(testLeague.getId()),
                        testLeague.getSchedulingMethod().toString(),
                        testLeague.getSchedulingMethod().toString()));
    }

    @Test
    public void getScoringMethods() throws UnsupportedEncodingException {
        MvcResult response = makeGetRequest("/api//association-rep/scoring-methods", apiMock);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(
                        Stream.of(ScoringMethod.values())
                                .map(ScoringMethod::toString)
                                .collect(Collectors.toList()));
    }

    @Test
    public void getSchedulingMethods() throws UnsupportedEncodingException {
        MvcResult response = makeGetRequest("/api//association-rep/scheduling-methods", apiMock);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(
                        Stream.of(SchedulingMethod.values())
                                .map(SchedulingMethod::toString)
                                .collect(Collectors.toList()));
    }

    @Test
    public void updatePolicy() {
        LeagueDTO dto = LeagueDTO.fromLeague(testLeague);
        dto.schedulingMethod = SchedulingMethod.SINGLE_FIXTURE;

        MvcResult response = makePostRequest("/api//association-rep/update-policy", apiMock,
                MediaType.APPLICATION_JSON, dto);

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());
        verify(associationRepService, times(1))
                .setSchedulingMethod(testLeague.getId(), SchedulingMethod.SINGLE_FIXTURE);
    }

    @Test
    public void getAllPendingRequests() throws UnsupportedEncodingException {
        TeamOwnerRegRequest request = new TeamOwnerRegRequest(simpleMember, "new team");
        when(associationRepService.getAllPendingRequests()).thenReturn(listOf(request));

        MvcResult response = makeGetRequest("/api/association-rep/pending-team-requests", apiMock);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString())
                .contains(listOf(String.valueOf(request.getId()), request.getRequestedTeamName()));
    }

    @Test
    public void handleNewTeamOwnerRequest() {
        // positive - request exists
        MvcResult response = makePostRequest(
                "/api/association-rep/handle-team-request", apiMock, MediaType.APPLICATION_JSON,
                listOf(Pair.of("memberID", "5"), Pair.of("approved", Boolean.toString(true))));
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());

        // negative - request doesn't exist
        doThrow(new IllegalArgumentException())
                .when(associationRepService)
                .handleNewTeamOwnerRequest(5, true);

        response = makePostRequest(
                "/api/association-rep/handle-team-request", apiMock, MediaType.APPLICATION_JSON,
                listOf(Pair.of("memberID", "5"), Pair.of("approved", Boolean.toString(true))));
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }
}