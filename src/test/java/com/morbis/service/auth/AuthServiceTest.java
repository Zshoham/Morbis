package com.morbis.service.auth;


import com.morbis.MorbisApplication;
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
import org.springframework.data.util.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { AuthService.class, MorbisApplication.class })
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
        authService.cleanAuthTable();
        testMember = Fan.newFan()
                .fromMember("jane", "pAssw0rD", "Jane Doe", "Jane@gmail.com")
                .withId(1)
                .build();

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        Member tempMember = Fan.newFan()
                .fromMember(testMember)
                .withId(testMember.getId())
                .build();
        tempMember.setPassword(
                passwordEncoder.encode(testMember.getPassword()));

        when(memberRepository.findDistinctByUsername("jane")).thenReturn(Optional.of(tempMember));
        when(memberRepository.findById(testMember.getId())).thenReturn(Optional.of(testMember));

        testAdmin = Admin.newAdmin()
                .fromMember("admin", "pAssw0rD123", "Jane Doe", "admin@gmail.com")
                .withId(2)
                .build();

        Admin tempAdmin = Admin.newAdmin()
                .fromMember(testAdmin)
                .withId(testAdmin.getId())
                .build();
        tempAdmin.setPassword(
                passwordEncoder.encode(testAdmin.getPassword()));
        when(memberRepository.findDistinctByUsername("admin")).thenReturn(Optional.of(tempAdmin));
        when(memberRepository.findById(testAdmin.getId())).thenReturn(Optional.of(testAdmin));
    }

    @Test
    public void register() {
        // test duplicate register
        Throwable potentialException = catchThrowable(() -> authService.register(testMember));
        assertThat(potentialException).hasMessageContaining("user already registered");
        // verifies that the method call "save(memberTest)" never does not occur.
        verify(memberRepository, never()).save(testMember);

        // test first register
        testMember.setUsername("NotUser");
        potentialException = catchThrowable(() -> authService.register(testMember));
        assertThat(potentialException).doesNotThrowAnyException();
        verify(memberRepository, times(1)).save(testMember);
    }

    @Test
    public void loginTest() {
        // token length
        Optional<Pair<Member, String>> firstLogin = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(firstLogin).isPresent();
        assertThat(firstLogin.get().getSecond().length()).isEqualTo(128); // token is 64 bytes and 128 characters is hex format

        // duplicate login
        Throwable possibleException = catchThrowable(() -> authService.login(testMember.getUsername(), testMember.getPassword()));
        assertThat(possibleException).hasMessage("user is already logged in");

        // invalid username
        Optional<Pair<Member, String>> invalidUsername = authService.login("baduser", "doesnotmatter");
        assertThat(invalidUsername).isEmpty();

        // invalid password
        Optional<Pair<Member, String>> invalidPassword = authService.login("user", "badpass");
        assertThat(invalidPassword).isEmpty();
    }

    @Test
    public void logout() {
        // make sure login is successful
        Optional<Pair<Member, String>> loginAns = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(loginAns).isPresent();

        // logout makes the token invalid.
        authService.logout(loginAns.get().getSecond());
        assertThat(authService.authorize(loginAns.get().getSecond(), MemberRole.FAN)).isFalse();

        // logout twice has no side effects
        Throwable secondLogout = catchThrowable(() -> authService.logout(loginAns.get().getSecond()));
        assertThat(secondLogout).doesNotThrowAnyException();
    }

    @Test
    public void authorizeTest() {
        // make sure login is successful
        Optional<Pair<Member, String>> fanLogin = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(fanLogin).isPresent();

        // make sure login is successful
        Optional<Pair<Member, String>> adminLogin = authService.login(testAdmin.getUsername(), testAdmin.getPassword());
        assertThat(adminLogin).isPresent();

        // authorize fan
        assertThat(authService.authorize(fanLogin.get().getSecond(), MemberRole.FAN)).isTrue();

        // authorize admin
        assertThat(authService.authorize(adminLogin.get().getSecond(), MemberRole.ADMIN)).isTrue();

        // authorize admin as fan
        assertThat(authService.authorize(adminLogin.get().getSecond(), MemberRole.FAN)).isTrue();
    }
}