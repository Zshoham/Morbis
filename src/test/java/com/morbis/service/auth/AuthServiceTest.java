package com.morbis.service.auth;

import com.goterl.lazycode.lazysodium.LazySodiumJava;
import com.goterl.lazycode.lazysodium.SodiumJava;
import com.goterl.lazycode.lazysodium.exceptions.SodiumException;
import com.morbis.model.member.entity.Admin;
import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import com.morbis.model.member.repository.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.goterl.lazycode.lazysodium.interfaces.PwHash.MEMLIMIT_INTERACTIVE;
import static com.goterl.lazycode.lazysodium.interfaces.PwHash.OPSLIMIT_INTERACTIVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

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
    public void setUp() throws SodiumException {
        authService.cleanAuthTable();
        testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .withId(1)
                .build();

        SodiumJava sodiumJava = new SodiumJava();
        LazySodiumJava lazySodium = new LazySodiumJava(sodiumJava);
        Member tempMember = Fan.newFan()
                .fromMember(testMember)
                .withId(1)
                .build();
        tempMember.setPassword(
                lazySodium.cryptoPwHashStr(testMember.getPassword(), OPSLIMIT_INTERACTIVE, MEMLIMIT_INTERACTIVE));

        when(memberRepository.findDistinctByUsername("user")).thenReturn(Optional.of(tempMember));
        when(memberRepository.findById(1)).thenReturn(Optional.of(testMember));

        testAdmin = Admin.newAdmin()
                .fromMember("admin", "pass", "name", "email")
                .withId(2)
                .build();

        Admin tempAdmin = Admin.newAdmin()
                .fromMember(testAdmin)
                .withId(2)
                .build();
        tempAdmin.setPassword(
                lazySodium.cryptoPwHashStr(testAdmin.getPassword(), OPSLIMIT_INTERACTIVE, MEMLIMIT_INTERACTIVE));
        when(memberRepository.findDistinctByUsername("admin")).thenReturn(Optional.of(tempAdmin));
        when(memberRepository.findById(2)).thenReturn(Optional.of(testAdmin));
    }

    @Test
    public void register() {
        // test duplicate register
        Throwable potentialException = catchThrowable(() -> authService.register(testMember));
        assertThat(potentialException).doesNotThrowAnyException();
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
        Optional<String> firstToken = authService.login(testMember.getUsername(), testMember.getPassword());
        assertThat(firstToken).isPresent();
        assertThat(firstToken.get().length()).isEqualTo(128); // token is 64 bytes and 128 characters is hex format

        // duplicate login
        Throwable possibleException = catchThrowable(() -> authService.login(testMember.getUsername(), testMember.getPassword()));
        assertThat(possibleException).hasMessage("user is already logged in");

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