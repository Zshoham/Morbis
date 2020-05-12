package com.morbis.api;

import com.morbis.api.dto.LoginDTO;
import com.morbis.api.dto.RegisterDTO;
import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.service.auth.AuthService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GuestControllerTest {

    @Autowired
    private MockMvc apiMock;

    @MockBean
    private AuthService authService;

    private String token;

    @Before
    public void setUp() throws Exception {
        Member testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .withId(1)
                .build();

        token = "q5we64qwe23q1we";

        when(authService.login(testMember.getUsername(), testMember.getPassword())).thenReturn(Optional.of(token));
        when(authService.register(any(Member.class))).thenReturn(true);
    }


    @Test
    public void register() throws Exception {
        RegisterDTO registerData = new RegisterDTO(
                "user", "pass", "name", "email");

        // positive test
        MvcResult response = TestUtils.makePostRequest(
                "/api/register", apiMock, MediaType.APPLICATION_JSON, registerData);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());

        // negative test
        when(authService.register(any())).thenReturn(false);
        response = TestUtils.makePostRequest(
                "/api/register", apiMock, MediaType.APPLICATION_JSON, registerData);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void login() throws UnsupportedEncodingException {
        LoginDTO loginData = new LoginDTO("user", "pass");

        // positive test
        MvcResult response = TestUtils.makePostRequest(
                "/api/login", apiMock, MediaType.APPLICATION_JSON, loginData);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString()).isEqualTo(token);

        // negative test
        loginData.username = "not correct";
        response = TestUtils.makePostRequest(
                "/api/login", apiMock, MediaType.APPLICATION_JSON, loginData);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getResponse().getContentAsString()).isEmpty();
    }

    @Test
    public void logout() {

        // positive test
        MvcResult response = TestUtils.makeGetRequest(
                "/api/logout/{token}", apiMock, token);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(authService, times(1)).logout(token);

        reset(authService);

        // negative test
        response = TestUtils.makeGetRequest(
                "/api/logout/{token}=", apiMock, "Invalid Token");
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        verify(authService, times(0)).logout(token);
    }
}