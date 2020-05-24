package com.morbis.model.member.repository;

import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.Coach;
import com.morbis.model.member.entity.Referee;
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
import static com.morbis.TestUtils.listOf;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RefereeRepositoryTest {

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

    @Autowired
    private RefereeRepository refereeRepository;

    @Autowired
    private GameRepository gameRepository;

    @Before
    public void setUp() {
        saveGame(playerRepository,
                teamOwnerRepository,
                coachRepository,
                stadiumRepository,
                managerRepository,
                teamRepository,
                refereeRepository,
                gameRepository);

        main.setMainGames(listOf(game));
        supporting.setSupportGames(listOf(game));
    }

    @Test
    public void save() {
        // saving the same referee twice has no effect.
        refereeRepository.save(main);
        assertThat(refereeRepository.findAll()).containsExactly(main, supporting);

        // saving a Game with the same id and changed content, will cause an update.
        main.setName("new name");
        refereeRepository.save(main);
        assertThat(refereeRepository.findAll()).containsExactly(main, supporting);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Referee> referee = refereeRepository.findById(main.getId());
        assertThat(referee).isPresent();
        assertThat(referee.get()).isEqualTo(main);

        // does not work with invalid id
        Optional<Referee> invalidReferee = refereeRepository.findById(999);
        assertThat(invalidReferee).isEmpty();
    }

    @Test
    public void findAllByNameContaining() {
        // works with correct username
        List<Referee> referees = refereeRepository.findAllByNameContaining(main.getName().substring(1, 5));
        assertThat(referees).containsExactly(main);

        // does not work with invalid username
        List<Coach> invalidCoach = coachRepository.findAllByNameContaining("invalid");
        assertThat(invalidCoach).isEmpty();
    }
}