package com.morbis.model.game.repository;

import com.morbis.model.game.entity.Game;
import com.morbis.model.member.repository.*;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
public class GameRepositoryTest {

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
    }

    @Test
    public void save() {
        // saving the same game twice has no effect.
        gameRepository.save(game);
        assertThat(gameRepository.findAll()).containsExactly(game);

        // saving a Game with the same id and changed content, will cause an update.
        game.setEndDate(LocalDateTime.now().plusMinutes(180));
        gameRepository.save(game);
        assertThat(gameRepository.findAll()).containsExactly(game);
    }

    @Test
    public void findAllContainingQuery() {
        // works with home team name
        List<Game> home = gameRepository.findAllContainingQuery(game.getHome().getName());
        assertThat(home).hasSize(1).containsExactly(game);

        // works with home team name
        List<Game> away = gameRepository.findAllContainingQuery(game.getAway().getName());
        assertThat(away).hasSize(1).containsExactly(game);

        // works with home team name
        List<Game> both = gameRepository.findAllContainingQuery(Game.getDescription(game));
        assertThat(both).hasSize(1).containsExactly(game);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Game> validGame = gameRepository.findById(game.getId());
        assertThat(validGame).isPresent();
        assertThat(validGame.get()).isEqualTo(game);

        // does not work with invalid id
        Optional<Game> invalidGame = gameRepository.findById(999);
        assertThat(invalidGame).isEmpty();
    }


}