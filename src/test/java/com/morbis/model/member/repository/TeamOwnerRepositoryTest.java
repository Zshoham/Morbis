package com.morbis.model.member.repository;

import com.morbis.model.member.entity.TeamOwner;
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
public class TeamOwnerRepositoryTest {

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

        homeOwner.setTeam(home);
        ownerRepository.save(homeOwner);
    }

    @Test
    public void save() {
        // saving the same TeamOwner twice has no effect.
        ownerRepository.save(homeOwner);
        assertThat(ownerRepository.findAll()).containsExactly(homeOwner, awayOwner);

        // saving a TeamOwner with the same id and changed content, will cause an update.
        homeOwner.setUsername("cool-TeamOwner");
        ownerRepository.save(homeOwner);
        assertThat(ownerRepository.findAll()).containsExactly(homeOwner, awayOwner);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<TeamOwner> teamOwner = ownerRepository.findById(homeOwner.getId());
        assertThat(teamOwner).isPresent();
        assertThat(teamOwner.get()).isEqualTo(homeOwner);

        // does not work with invalid id
        Optional<TeamOwner> invalidTeamOwner = ownerRepository.findById(999);
        assertThat(invalidTeamOwner).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct username
        List<TeamOwner> owners = ownerRepository.findAllByNameContaining(homeOwner.getName().substring(2));
        assertThat(owners).containsExactly(homeOwner);

        // does not work with invalid username
        List<TeamOwner> invalidTeamOwner = ownerRepository.findAllByNameContaining("invalid");
        assertThat(invalidTeamOwner).isEmpty();
    }
}