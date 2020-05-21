package com.morbis.model.poster.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.poster.entity.PosterData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.morbis.data.ViewableEntitySource.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PosterDataRepositoryTest {

    @Autowired
    private PosterDataRepository posterDataRepository;
    @Autowired
    private MemberRepository memberRepository;

    private PosterData testPosterData;
    LinkedList<Member> followers;

    @Before
    public void setUp() {
        initWithoutID();
        followers = new LinkedList<>();
        followers.add(homeCoach.asBashFan());
        followers.add(awayCoach.asBashFan());
        followers.add(awayPlayer.asBashFan());
        memberRepository.saveAll(followers);
        testPosterData = PosterData.newPosterData()
                .withFollowers(followers)
                .build();
        posterDataRepository.save(testPosterData);
    }

    @Test
    public void save() {
        // saving the same member twice has no effect.
        posterDataRepository.save(testPosterData);
        assertThat(posterDataRepository.findAll()).containsExactly(testPosterData);

        // saving poster data with the same id and changed content, will cause an update.
        followers.add(memberRepository.save(homePlayer.asBashFan()));
        testPosterData.setFollowers(followers);
        posterDataRepository.save(testPosterData);
        assertThat(posterDataRepository.findAll()).containsExactly(testPosterData);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<PosterData> poster = posterDataRepository.findById(testPosterData.getId());
        assertThat(poster).isPresent();
        assertThat(poster.get()).isEqualTo(testPosterData);

        // does not work with invalid id
        Optional<PosterData> invalidPoster = posterDataRepository.findById(testPosterData.getId() + 1);
        assertThat(invalidPoster).isEmpty();
    }
}