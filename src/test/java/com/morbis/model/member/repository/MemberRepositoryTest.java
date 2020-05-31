package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import com.morbis.model.member.entity.MemberSearch;
import com.morbis.model.poster.entity.PosterData;
import com.morbis.model.poster.repository.PosterDataRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.morbis.TestUtils.listOf;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PosterDataRepository posterDataRepository;

    @Autowired
    private MemberSearchRepository memberSearchRepository;

    private Member testMember;

    @Before
    public void setUp() {
        PosterData page = PosterData.newPosterData().build();
        posterDataRepository.save(page);

        testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .build();
        memberRepository.save(testMember);


        testMember.setPagesFollowing(listOf(page));
        MemberSearch search = new MemberSearch("nuggets", testMember);
        testMember.setSearches(listOf(search));

        memberSearchRepository.save(search);
        memberRepository.save(testMember);
    }

    @Test
    public void save() {
        // saving the same member twice has no effect.
        memberRepository.save(testMember);
        assertThat(memberRepository.findAll()).containsExactly(testMember);

        // saving a member with the same id and changed content, will cause an update.
        testMember.setUsername("secondUser");
        memberRepository.save(testMember);
        assertThat(memberRepository.findAll()).containsExactly(testMember);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Member> member = memberRepository.findById(testMember.getId());
        assertThat(member).isPresent();
        assertThat(member.get()).isEqualTo(testMember);

        // does not work with invalid id
        Optional<Member> invalidMember = memberRepository.findById(testMember.getId() + 1);
        assertThat(invalidMember).isEmpty();
    }

    @Test
    public void findDistinctByUsername() {
        // works with correct username
        Optional<Member> member = memberRepository.findDistinctByUsername(testMember.getUsername());
        assertThat(member).isPresent();
        assertThat(member.get()).isEqualTo(testMember);

        // does not work with invalid username
        Optional<Member> invalidMember = memberRepository.findDistinctByUsername("invalid");
        assertThat(invalidMember).isEmpty();
    }

    @Test
    public void findAllByMemberRoleNotIn() {
        // finds members correctly.
        List<Member> members = memberRepository.findAllByMemberRoleNotIn(listOf(MemberRole.TEAM_OWNER, MemberRole.TEAM_MANAGER));
        assertThat(members).containsExactly(testMember);

        // does not find incorrect members.
        members = memberRepository.findAllByMemberRoleNotIn(listOf(MemberRole.FAN));
        assertThat(members).isEmpty();
    }
}