package com.morbis.model.league.entity;

import com.morbis.model.game.entity.Game;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Season {
    @Id
    @GeneratedValue
    private int id;

    public Season(int id, int year) {
        setId(id);
        setYear(year);
    }

    public Season(int year) {
        setYear(year);
    }

    @Range(min = 1900)
    private int year;

    @OneToMany(targetEntity = Game.class)
    private List<Game> games;


    public static SeasonBuilder newSeason(int year) {
        return new SeasonBuilder(year);
    }

    public static class SeasonBuilder {

        private final Season result;

        public SeasonBuilder(int year) {
            result = new Season(year);
        }

        public SeasonBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public SeasonBuilder withSeasons(List<Game> seasons) {
            result.setGames(seasons);
            return this;
        }

        public Season build() {
            return result;
        }
    }

}
