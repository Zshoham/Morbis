package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member testMember;

    @Before
    public void setUp() {
        testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .build();

        memberRepository.save(testMember);
    }

    @Test
    @Rollback
    public void save() {
        //TODO: WHY ARE YOU RUNNING ?
        // saving the same member twice has no effect.
        memberRepository.save(testMember);
        assertThat(memberRepository.findAll()).containsExactly(testMember);

        // saving a member with the same id twice has no effect.
        testMember.setUsername("secondUser");
        memberRepository.save(testMember);
        assertThat(memberRepository.findAll()).containsExactly(testMember);
    }

    @Test
    public void findDistinctByUsername() {
        // works with correct username
        Optional<Member> fan = memberRepository.findDistinctByUsername(testMember.getUsername());
        assertThat(fan).isPresent();
        assertThat(fan.get()).isEqualTo(testMember);

        // does not work with invalid username
        Optional<Member> invalidFan = memberRepository.findDistinctByUsername("invalid");
        assertThat(invalidFan).isEmpty();
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Member> fan = memberRepository.findById(testMember.getId());
        assertThat(fan).isPresent();
        assertThat(fan.get()).isEqualTo(testMember);

        // does not work with invalid id
        Optional<Member> invalidFan = memberRepository.findById(testMember.getId() + 1);
        assertThat(invalidFan).isEmpty();
    }
}