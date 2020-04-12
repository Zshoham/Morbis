package com.morbis.model.game.repository;

import com.morbis.data.ViewableEntitySource;
import com.morbis.model.game.entity.Game;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;



@RunWith(SpringRunner.class)
@DataJpaTest
@Ignore
public class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;

    private Game testGame;


    @Before
    public void setUp() throws Exception {
        ViewableEntitySource.initWithoutID();
        testGame = Game.newGame()
                .teams(home, away)
                .time(LocalDateTime.now())
                .refs(main, Collections.singletonList(supporting))
                .build();

        gameRepository.save(testGame);
        gameRepository.flush();
        System.out.println("FINISHED FIRST SAVE");
    }

    @Test
    public void save() {
        // saving the same game twice has no effect.
        gameRepository.save(testGame);
        assertThat(gameRepository.findAll()).containsExactly(testGame);
    }

    @Test
    public void findAllContainingQuery() {

        // works with home team name
        List<Game> home = gameRepository.findAllContainingQuery(testGame.getHome().getName());
        assertThat(home).hasSize(1).containsExactly(testGame);

        // works with home team name
        List<Game> away = gameRepository.findAllContainingQuery(testGame.getAway().getName());
        assertThat(home).hasSize(1).containsExactly(testGame);

        // works with home team name
        List<Game> both = gameRepository.findAllContainingQuery(Game.getDescription(game));
        assertThat(home).hasSize(1).containsExactly(testGame);
    }

    @Test
    public void findById() {

        // works with correct id
        Optional<Game> game = gameRepository.findById(testGame.getId());
        assertThat(game).isPresent();
        assertThat(game.get()).isEqualTo(testGame);

        // does not work with invalid id
        Optional<Game> invalidGame = gameRepository.findById(testGame.getId() + 1);
        assertThat(invalidGame).isEmpty();
    }


}