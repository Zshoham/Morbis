package com.morbis.model.league.repository;

import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.league.entity.Season;
import com.morbis.model.member.repository.*;
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
import static com.morbis.TestUtils.listOf;

@RunWith(SpringRunner.class)
@DataJpaTest
public class SeasonRepositoryTest {

    @Autowired private TeamRepository teamRepository;
    @Autowired private PlayerRepository playerRepository;
    @Autowired private TeamOwnerRepository teamOwnerRepository;
    @Autowired private CoachRepository coachRepository;
    @Autowired private StadiumRepository stadiumRepository;
    @Autowired private TeamManagerRepository managerRepository;
    @Autowired private RefereeRepository refereeRepository;
    @Autowired private GameRepository gameRepository;
    @Autowired private LeagueRepository leagueRepository;
    @Autowired private SeasonRepository seasonRepository;

    @Before
    public void setUp() {
        saveAll(playerRepository,
                teamOwnerRepository,
                coachRepository,
                stadiumRepository,
                managerRepository,
                teamRepository,
                refereeRepository,
                gameRepository,
                leagueRepository,
                seasonRepository);

        season.setReferees(listOf(main, supporting));
        seasonRepository.save(season);
    }


    @Test
    public void save() {
        // saving the same season twice has no effect.
        seasonRepository.save(season);
        assertThat(seasonRepository.findAll()).containsExactly(season);

        // saving a season with the same id and changed content, will cause an update.
        season.setYear(2019);
        seasonRepository.save(season);
        assertThat(seasonRepository.findAll()).containsExactly(season);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Season> mySeason = seasonRepository.findById(season.getId());
        assertThat(mySeason).isPresent();
        assertThat(mySeason.get()).isEqualTo(season);

        // does not work with invalid id
        Optional<Season> invalidSeason = seasonRepository.findById(999);
        assertThat(invalidSeason).isEmpty();
    }

    @Test
    public void findAllByYear() {
        // works with correct year
        List<Season> results = seasonRepository.findAllByYear(season.getYear());
        assertThat(results).containsExactly(season);

        // does not work with invalid year
        results = seasonRepository.findAllByYear(9999);
        assertThat(results).isEmpty();
    }

    @Test
    public void findByLeagueAndYear() {
        // works with correct league and year
        Optional<Season> mySeason = seasonRepository.findByLeagueAndYear(season.getLeague(), season.getYear());
        assertThat(mySeason).isPresent();
        assertThat(mySeason.get()).isEqualTo(season);

        // does not work with invalid league and year
        Optional<Season> invalidSeason = seasonRepository.findByLeagueAndYear(season.getLeague(),
                                                                         season.getYear() + 1);
        assertThat(invalidSeason).isEmpty();
    }
}