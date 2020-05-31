package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Coach;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
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
public class CoachRepositoryTest {

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

        homeCoach.setTeam(home);
        coachRepository.save(homeCoach);
    }

    @Test
    public void save() {
        // saving the same coach twice has no effect.
        coachRepository.save(homeCoach);
        assertThat(coachRepository.findAll()).containsExactly(homeCoach, awayCoach);

        // saving a coach with the same id and changed content, will cause an update.
        homeCoach.setUsername("cool-Coach");
        coachRepository.save(homeCoach);
        assertThat(coachRepository.findAll()).containsExactly(homeCoach, awayCoach);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Coach> Coach = coachRepository.findById(homeCoach.getId());
        assertThat(Coach).isPresent();
        assertThat(Coach.get()).isEqualTo(homeCoach);

        // does not work with invalid id
        Optional<Coach> invalidCoach = coachRepository.findById(999);
        assertThat(invalidCoach).isEmpty();
    }


    @Test
    public void findAllByNameContaining() {
        // works with correct username
        List<Coach> coaches = coachRepository.findAllByNameContaining(homeCoach.getName().substring(2));
        assertThat(coaches).containsExactly(homeCoach);

        // does not work with invalid username
        List<Coach> invalidCoach = coachRepository.findAllByNameContaining("invalid");
        assertThat(invalidCoach).isEmpty();
    }

    @Test
    public void findAllByTeamIsNull() {
        List<Coach> results = coachRepository.findAllByTeamIsNull();
        assertThat(results).containsExactly(awayCoach);
    }
}