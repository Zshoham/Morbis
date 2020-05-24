package com.morbis.model.league.repository;

import com.morbis.model.league.entity.League;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class LeagueRepositoryTest {

    @Autowired
    private LeagueRepository leagueRepository;

    private League testLeague;

    @Before
    public void setUp() {
        testLeague = League.newLeague("league").build();
        leagueRepository.save(testLeague);
    }

    @Test
    public void save() {
        // saving the same league twice has no effect.
        leagueRepository.save(testLeague);
        assertThat(leagueRepository.findAll()).containsExactly(testLeague);

        // saving a league with the same id and changed content, will cause an update.
        testLeague.setName("league-2020");
        leagueRepository.save(testLeague);
        assertThat(leagueRepository.findAll()).containsExactly(testLeague);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<League> myLeague = leagueRepository.findById(testLeague.getId());
        assertThat(myLeague).isPresent();
        assertThat(myLeague.get()).isEqualTo(testLeague);

        // does not work with invalid id
        Optional<League> invalidLeague = leagueRepository.findById(testLeague.getId() + 1);
        assertThat(invalidLeague).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct name
        List<League> results = leagueRepository.findAllByNameContaining(testLeague.getName().substring(1, 3));
        assertThat(results).containsExactly(testLeague);

        // does not work with invalid name
        results = leagueRepository.findAllByNameContaining("invalid");
        assertThat(results).isEmpty();
    }

    @Test
    public void findAllByName() {
        // works with correct name
        List<League> results = leagueRepository.findAllByName(testLeague.getName());
        assertThat(results).containsExactly(testLeague);

        // does not work with invalid name
        results = leagueRepository.findAllByName("invalid");
        assertThat(results).isEmpty();
    }
}