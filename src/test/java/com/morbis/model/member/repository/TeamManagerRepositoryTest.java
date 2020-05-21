package com.morbis.model.member.repository;

import com.morbis.model.member.entity.TeamManager;
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
public class TeamManagerRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamOwnerRepository ownerRepository;

    @Autowired
    private CoachRepository coachRepository;

    @Autowired
    private StadiumRepository stadiumRepository;

    @Autowired
    private TeamManagerRepository managerRepository;

    @Before
    public void setUp() {
        saveTeams(playerRepository,
                ownerRepository,
                coachRepository,
                stadiumRepository,
                managerRepository,
                teamRepository);

        homeManager.setTeam(home);
        managerRepository.save(homeManager);
    }

    @Test
    public void save() {
        // saving the same team manager twice has no effect.
        managerRepository.save(homeManager);
        assertThat(managerRepository.findAll()).containsExactly(homeManager, awayManager);

        // saving a team manager with the same id and changed content, will cause an update.
        homeManager.setUsername("cool-TeamOwner");
        managerRepository.save(homeManager);
        assertThat(managerRepository.findAll()).containsExactly(homeManager, awayManager);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<TeamManager> teamManager = managerRepository.findById(homeManager.getId());
        assertThat(teamManager).isPresent();
        assertThat(teamManager.get()).isEqualTo(homeManager);

        // does not work with invalid id
        Optional<TeamManager> invalidManager = managerRepository.findById(999);
        assertThat(invalidManager).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct username
        List<TeamManager> managers = managerRepository.findAllByNameContaining(homeManager.getName().substring(2));
        assertThat(managers).containsExactly(homeManager, awayManager);

        // does not work with invalid username
        List<TeamManager> invalidManagers = managerRepository.findAllByNameContaining("invalid");
        assertThat(invalidManagers).isEmpty();
    }
}