package com.morbis.model.team.repository;

import com.morbis.model.member.repository.CoachRepository;
import com.morbis.model.member.repository.PlayerRepository;
import com.morbis.model.member.repository.TeamManagerRepository;
import com.morbis.model.member.repository.TeamOwnerRepository;
import com.morbis.model.team.entity.Stadium;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StadiumRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamOwnerRepository teamOwnerRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamManagerRepository managerRepository;

    @Before
    public void setUp() {
        saveTeams(playerRepository,
                teamOwnerRepository,
                coachRepository,
                stadiumRepository,
                managerRepository,
                teamRepository);

        homeStadium.setTeam(home);
        stadiumRepository.save(homeStadium);
    }

    @Test
    public void save() {
        // saving the same member twice has no effect.
        stadiumRepository.save(homeStadium);
        assertThat(stadiumRepository.findAll()).containsExactly(homeStadium, awayStadium);

        // saving a member with the same id and changed content, will cause an update.
        homeStadium.setName("name");
        stadiumRepository.save(homeStadium);
        assertThat(stadiumRepository.findAll()).containsExactly(homeStadium, awayStadium);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Stadium> stadium = stadiumRepository.findById(homeStadium.getId());
        assertThat(stadium).isPresent();
        assertThat(stadium.get()).isEqualTo(homeStadium);

        // does not work with invalid id
        Optional<Stadium> invalidStadium = stadiumRepository.findById(999);
        assertThat(invalidStadium).isEmpty();
    }
    @Test
    public void findDistinctByName() {
        // works with correct name
        Optional<Stadium> stadium = stadiumRepository.findDistinctByName(homeStadium.getName());
        assertThat(stadium).isPresent();
        assertThat(stadium.get()).isEqualTo(homeStadium);

        // does not work with invalid name
        Optional<Stadium> invalidStadium = stadiumRepository.findDistinctByName("invalid");
        assertThat(invalidStadium).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct name
        List<Stadium> results = stadiumRepository.findAllByNameContaining(homeStadium.getName().substring(1, 4));
        assertThat(results).containsExactly(homeStadium);

        // does not work with invalid name
        results = stadiumRepository.findAllByNameContaining("invalid");
        assertThat(results).isEmpty();
    }

    @Test
    public void findAllByTeamIsNull() {
        List<Stadium> results = stadiumRepository.findAllByTeamIsNull();
        assertThat(results).containsExactly(awayStadium);
    }
}