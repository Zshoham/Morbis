package com.morbis.service.viewable;

import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.Season;
import com.morbis.model.member.entity.Coach;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import org.junit.Test;

import static com.morbis.service.viewable.ViewableEntityType.*;
import static org.assertj.core.api.Assertions.assertThat;


public class ViewableEntityTypeTest {


    @Test
    public void match() {
        boolean[] flags = new boolean[10];

        ViewableEntityType.matchAll()
                .inCase(GAME,            () -> flags[0] = true)
                .inCase(LEAGUE,          () -> flags[1] = true)
                .inCase(SEASON,          () -> flags[2] = true)
                .inCase(COACH,           () -> flags[3] = true)
                .inCase(PLAYER,          () -> flags[4] = true)
                .inCase(REFEREE,         () -> flags[5] = true)
                .inCase(TEAM_OWNER,      () -> flags[6] = true)
                .inCase(TEAM_MANAGER,    () -> flags[7] = true)
                .inCase(STADIUM,         () -> flags[8] = true)
                .inCase(TEAM,            () -> flags[9] = true)
                .execute();

        assertThat(flags).containsOnly(true);

        for (int i = 0; i < 10; i++)
            flags[i] = false;

        ViewableEntityType.match(GAME, TEAM)
                .inCase(GAME,            () -> flags[0] = true)
                .inCase(LEAGUE,          () -> flags[1] = true)
                .inCase(SEASON,          () -> flags[2] = true)
                .inCase(COACH,           () -> flags[3] = true)
                .inCase(PLAYER,          () -> flags[4] = true)
                .execute();

        assertThat(flags).containsOnlyOnce(true);
    }


    @Test
    public void classValidator() {
        ClassValidator validator = ViewableEntityType.classValidatorFor(TEAM, COACH, STADIUM);

        assertThat(validator.validate(Team.class)).isTrue();
        assertThat(validator.validate(Coach.class)).isTrue();
        assertThat(validator.validate(Stadium.class)).isTrue();

        assertThat(validator.validate(Season.class)).isFalse();
        assertThat(validator.validate(Referee.class)).isFalse();
        assertThat(validator.validate(League.class)).isFalse();
    }

}