package com.morbis.model.team.repository;

import com.morbis.model.member.repository.*;
import com.morbis.model.team.entity.Team;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.morbis.data.ViewableEntitySource.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamRepositoryTest {

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
    }

    @Test
    public void save() {
        // saving the same Team twice has no effect.
        teamRepository.save(home);
        assertThat(teamRepository.findAll()).containsExactly(home, away);

        // saving a Team with the same id and changed content, will cause an update.
        home.setName("cool-Team");
        teamRepository.save(home);
        assertThat(teamRepository.findAll()).containsExactly(home, away);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Team> Team = teamRepository.findById(home.getId());
        assertThat(Team).isPresent();
        assertThat(Team.get()).isEqualTo(home);

        // does not work with invalid id
        Optional<Team> invalidTeam = teamRepository.findById(999);
        assertThat(invalidTeam).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct username
        List<Team> teams = teamRepository.findAllByNameContaining(home.getName().substring(2));
        assertThat(teams).containsExactly(home);

        // does not work with invalid username
        List<Team> invalidTeam = teamRepository.findAllByNameContaining("invalid");
        assertThat(invalidTeam).isEmpty();
    }
}