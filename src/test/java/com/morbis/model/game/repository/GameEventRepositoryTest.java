package com.morbis.model.game.repository;

import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GameEventRepositoryTest {
    @Autowired
    private GameEventRepository gameEventRepository;

    private GameEvent testGameEvent;

    @Before
    public void setUp() {
        testGameEvent = GameEvent.newGameEvent()
                .type(GameEventType.GOAL)
                .time(LocalDateTime.now(), 5)
                .description("description")
                .build();
        gameEventRepository.save(testGameEvent);
    }

    @Test
    public void save() {
        // saving the same game event twice has no effect.
        gameEventRepository.save(testGameEvent);
        assertThat(gameEventRepository.findAll()).containsExactly(testGameEvent);

        // saving a game event with the same id and changed content, will cause an update.
        testGameEvent.setType(GameEventType.RED_CARD);
        gameEventRepository.save(testGameEvent);
        assertThat(gameEventRepository.findAll()).containsExactly(testGameEvent);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<GameEvent> myGameEvent = gameEventRepository.findById(testGameEvent.getId());
        assertThat(myGameEvent).isPresent();
        assertThat(myGameEvent.get()).isEqualTo(testGameEvent);

        // does not work with invalid id
        Optional<GameEvent> invalidGameEvent = gameEventRepository.findById(testGameEvent.getId() + 1);
        assertThat(invalidGameEvent).isEmpty();
    }
}