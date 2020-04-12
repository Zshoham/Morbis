package com.morbis.service.auth;

import com.morbis.model.member.entity.Admin;
import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import com.morbis.model.member.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = AuthService.class)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private MemberRepository memberRepository;

    // basic member
    private Member testMember;

    // member with permissions.
    private Admin testAdmin;

    @Before
    public void setUp() {
        testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .withId(1)
                .build();
        when(memberRepository.findDistinctByUsername("user")).thenReturn(Optional.of(testMember));
        when(memberRepository.findById(1)).thenReturn(Optional.of(testMember));

        testAdmin = Admin.newAdmin()
                .fromMember("admin", "pass", "name", "email")
                .withId(2)
                .build();
        when(memberRepository.findDistinctByUsername("admin")).thenReturn(Optional.of(testAdmin));
        when(memberRepository.findById(2)).thenReturn(Optional.of(testAdmin));
    }

    @Test
    public void loginTest() {
        // token length
        Optional<String> firstToken = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(firstToken).isPresent();
        assertThat(firstToken.get().length()).isEqualTo(512);

        // duplicate login
        Optional<String> secondToken = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(secondToken).isNotEqualTo(firstToken);

        // invalid username
        Optional<String> invalidUsername = authService.login("baduser", "doesnotmatter");
        assertThat(invalidUsername).isEmpty();

        // invalid password
        Optional<String> invalidPassword = authService.login("user", "badpass");
        assertThat(invalidPassword).isEmpty();
    }

    @Test
    public void logout() {
        // make sure login is successful
        Optional<String> token = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(token).isPresent();

        // logout makes the token invalid.
        authService.logout(token.get());
        assertThat(authService.authorize(token.get(), MemberRole.FAN)).isFalse();

        // logout twice has no side effects
        Throwable secondLogout = catchThrowable(() -> authService.logout(token.get()));
        assertThat(secondLogout).doesNotThrowAnyException();
    }

    @Test
    public void authorizeTest() {
        // make sure login is successful
        Optional<String> fanToken = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(fanToken).isPresent();

        // make sure login is successful
        Optional<String> adminToken = authService.login(testAdmin.getUsername(), testAdmin.getPassword());
        assertThat(adminToken).isPresent();

        // authorize fan
        assertThat(authService.authorize(fanToken.get(), MemberRole.FAN)).isTrue();

        // authorize admin
        assertThat(authService.authorize(adminToken.get(), MemberRole.ADMIN)).isTrue();

        // authorize admin as fan
        assertThat(authService.authorize(adminToken.get(), MemberRole.FAN)).isTrue();
    }
}