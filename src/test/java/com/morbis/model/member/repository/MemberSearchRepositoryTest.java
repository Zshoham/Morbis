package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberSearch;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.morbis.TestUtils.listOf;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberSearchRepositoryTest {

    @Autowired
    private MemberSearchRepository memberSearchRepository;

    @Autowired
    private MemberRepository memberRepository;

    private MemberSearch testSearch;

    @Before
    public void setUp() {
        Member testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .build();
        memberRepository.save(testMember);


        testSearch = new MemberSearch("nuggets", testMember);
        testMember.setSearches(listOf(testSearch));

        memberSearchRepository.save(testSearch);
        memberRepository.save(testMember);
    }

    @Test
    public void save() {
        // saving the same search twice has no effect.
        memberSearchRepository.save(testSearch);
        assertThat(memberSearchRepository.findAll()).containsExactly(testSearch);

        // saving a search with the same id and changed content, will cause an update.
        testSearch.setQuery("different query");
        memberSearchRepository.save(testSearch);
        assertThat(memberSearchRepository.findAll()).containsExactly(testSearch);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<MemberSearch> search = memberSearchRepository.findById(testSearch.getId());
        assertThat(search).isPresent();
        assertThat(search.get()).isEqualTo(testSearch);

        // does not work with invalid id
        Optional<MemberSearch> invalidMember = memberSearchRepository.findById(testSearch.getId() + 1);
        assertThat(invalidMember).isEmpty();
    }
}