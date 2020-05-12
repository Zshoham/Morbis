package com.morbis.service.auth;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class AuthTableTest {

    private AuthTable testTable;

    private Member testMember;

    @Before
    public void setUp() {
        testTable = new AuthTable();
        testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .withId(1)
                .build();
    }

    @Test
    public void createToken() {
        var counter = new Object() { int value; };
        List<String> tokens = Stream.generate(() ->
                testTable.createToken(
                        Fan.newFan()
                                .fromMember(testMember)
                                .withId(counter.value++)
                                .build()))
                .limit(100).collect(Collectors.toList());

        // tokens are unique.
        assertThat(tokens).allSatisfy(token ->
                assertThat(tokens).containsOnlyOnce(token));

        // tokens are of length 64 bytes or 128 characters in hex representation
        assertThat(tokens).allSatisfy(token -> assertThat(token.length()).isEqualTo(128));
    }

    @Test
    public void validate() {
        String token = testTable.createToken(testMember);

        // validate for token works
        assertThat(testTable.validate(token)).isPresent().hasValue(1);

        // does not validate for invalid token
        assertThat(testTable.validate("not valid")).isEmpty();
    }

    @Test
    public void removeToken() {
        // make sure token is added
        String token = testTable.createToken(testMember);
        assertThat(testTable.validate(token)).isPresent().hasValue(1);

        // token removed successfully
        testTable.removeToken(token);
        assertThat(testTable.validate(token)).isEmpty();

        // removing twice has no effect.
        Throwable secondLogout = catchThrowable(() -> testTable.removeToken(token));
        assertThat(secondLogout).doesNotThrowAnyException();

    }
}