package com.morbis.model.league.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.morbis.model.game.entity.Game;
import com.morbis.model.member.entity.Referee;
import lombok.*;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Season {

    @Id
    @GeneratedValue
    private int id;

    public Season(int id, int year, League league) {
        setId(id);
        setYear(year);
        setLeague(league);
    }

    public Season(int year, League league) {
        setYear(year);
        setLeague(league);
    }

    @Range(min = 1900)
    private int year;

    @ManyToOne(targetEntity = League.class)
    @JsonBackReference
    private League league;

    @OneToMany(targetEntity = Game.class)
    private List<Game> games;

    @ManyToMany(targetEntity = Referee.class)
    @JsonManagedReference
    private List<Referee> referees;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Season)) return false;

        Season season = (Season) o;

        if (id != season.id) return false;
        if (year != season.year) return false;
        return league.equals(season.league);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + year;
        result = 31 * result + league.hashCode();
        return result;
    }

    public static SeasonBuilder newSeason(int year, League league) {
        return new SeasonBuilder(year, league);
    }

    public static class SeasonBuilder {

        private final Season result;

        public SeasonBuilder(int year, League league) {
            result = new Season(year, league);
        }

        public SeasonBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public SeasonBuilder withGames(List<Game> seasons) {
            result.setGames(seasons);
            return this;
        }

        public SeasonBuilder withReferees(List<Referee> referees) {
            result.setReferees(referees);
            return this;
        }

        public Season build() {
            return result;
        }
    }

}
