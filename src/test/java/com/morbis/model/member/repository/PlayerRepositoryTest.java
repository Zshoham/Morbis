package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Player;
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
public class PlayerRepositoryTest {

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

        homePlayer.setTeam(home);
        playerRepository.save(homePlayer);
    }

    @Test
    public void save() {
        // saving the same player twice has no effect.
        playerRepository.save(homePlayer);
        assertThat(playerRepository.findAll()).containsExactly(homePlayer, awayPlayer);

        // saving a player with the same id and changed content, will cause an update.
        homePlayer.setUsername("cool-Player");
        playerRepository.save(homePlayer);
        assertThat(playerRepository.findAll()).containsExactly(homePlayer, awayPlayer);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Player> player = playerRepository.findById(homePlayer.getId());
        assertThat(player).isPresent();
        assertThat(player.get()).isEqualTo(homePlayer);

        // does not work with invalid id
        Optional<Player> invalidPlayer = playerRepository.findById(999);
        assertThat(invalidPlayer).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct username
        List<Player> players = playerRepository.findAllByNameContaining(homePlayer.getName().substring(2));
        assertThat(players).containsExactly(homePlayer, awayPlayer);

        // does not work with invalid username
        List<Player> invalidPlayer = playerRepository.findAllByNameContaining("invalid");
        assertThat(invalidPlayer).isEmpty();
    }

    @Test
    public void findAllByTeamIsNull() {
        List<Player> results = playerRepository.findAllByTeamIsNull();
        assertThat(results).containsExactly(awayPlayer);
    }
}