package com.morbis.api;

import com.morbis.TestUtils;
import com.morbis.api.dto.LoginDTO;
import com.morbis.api.dto.LoginResponseDTO;
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
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private LoginResponseDTO loginAns;

    @Before
    public void setUp(){
        Member testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .withId(1)
                .build();

        token = "q5we64qwe23q1we";
        loginAns = new LoginResponseDTO(token, testMember.getMemberRole(), testMember.getId());

        when(authService.login(testMember.getUsername(), testMember.getPassword())).
                thenReturn(Optional.of(Pair.of(testMember, token)));

    }


    @Test
    public void register() {
        // positive test
        assertThat(registerValidation("jane", "pAssw0rD", "Jane Doe", "Jane@gmail.com")
                .getResponse().getStatus()).isEqualTo(HttpStatus.ACCEPTED.value());

        // negative test- user exist
        RegisterDTO registerData = new RegisterDTO(
                "jane", "pAssw0rD", "Jane Doe", "Jane@gmail.com");
        doThrow(new IllegalArgumentException("user already registered")).when(authService).register(any());
        MvcResult response = TestUtils.makePostRequest(
                "/api/register", apiMock, MediaType.APPLICATION_JSON, registerData);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // negative test- invalid user data
            // invalid username- starts with non alphabetic letter
        assertThat(registerValidation("55jane", "pAssw0rD", "Jane Doe", "Jane@gmail.com")
                .getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            // invalid password- too short
        assertThat(registerValidation("jane", "Zaq1", "Jane Doe", "Jane@gmail.com")
                .getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            // invalid name- too short
        assertThat(registerValidation("jane", "pAssw0rD", "Ja", "Jane@gmail.com")
                .getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            // invalid email- not in correct format
        assertThat(registerValidation("jane", "pAssw0rD", "Jane Doe", "Janegmailcom")
                .getResponse().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());

    }

    private MvcResult registerValidation(String username, String password, String name, String email){
        RegisterDTO registerData = new RegisterDTO(
                username, password, name, email);
        return TestUtils.makePostRequest(
                "/api/register", apiMock, MediaType.APPLICATION_JSON, registerData);
    }

    @Test
    public void login() throws UnsupportedEncodingException {
        LoginDTO loginData = new LoginDTO("user", "pass");

        // positive test
        MvcResult response = TestUtils.makePostRequest(
                "/api/login", apiMock, MediaType.APPLICATION_JSON, loginData);
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getResponse().getContentAsString()).isEqualTo(
                "{\"token\":\"q5we64qwe23q1we\",\"roles\":[\"FAN\"],\"memberID\":1}");

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