package com.morbis.model.game.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.team.entity.Team;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Game {

    private static final int GAME_LENGTH = 90;

    public static String getDescription(Game game) {
        return game.getHome().getName().concat(" V ")
                .concat(game.getAway().getName());
    }

    @Id
    @GeneratedValue
    private int id;

    public Game(int id, Team home, Team away, LocalDateTime startDate, Referee mainRef, List<Referee> supportingRefs) {
        setId(id);
        setHome(home);
        setAway(away);
        setStartDate(startDate);
        setEndDate(startDate.plusMinutes(90));
        setMainRef(mainRef);
        setSupportingRefs(supportingRefs);
    }

    public Game(Team home, Team away, LocalDateTime startDate, Referee mainRef, List<Referee> supportingRefs) {
        setHome(home);
        setAway(away);
        setStartDate(startDate);
        setEndDate(startDate.plusMinutes(90));
        setMainRef(mainRef);
        setSupportingRefs(supportingRefs);
    }

    @ManyToOne(targetEntity = Team.class, cascade = CascadeType.ALL)
    private Team home;

    @ManyToOne(targetEntity = Team.class, cascade = CascadeType.ALL)
    private Team away;

    @ManyToMany(targetEntity = Member.class)
    private List<Member> followers;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @OneToMany(targetEntity = GameEvent.class, cascade = CascadeType.ALL)
    private List<GameEvent> events;

    @ManyToOne(targetEntity = Referee.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Referee mainRef;

    @ManyToMany(targetEntity = Referee.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Referee> supportingRefs;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        if (id != game.id) return false;
        if (!home.equals(game.home)) return false;
        if (!away.equals(game.away)) return false;
        if (!startDate.equals(game.startDate)) return false;
        return endDate.equals(game.endDate);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + home.hashCode();
        result = 31 * result + away.hashCode();
        result = 31 * result + startDate.hashCode();
        result = 31 * result + endDate.hashCode();
        return result;
    }

    public static GameTeamsBuilder newGame() {
        return new GameBuilder();
    }

    public interface GameTeamsBuilder { GameTimeBuilder teams(Team home, Team away); }

    public interface GameTimeBuilder {
        GameRefsBuilder time(LocalDateTime startTime, LocalDateTime endTime);
        GameRefsBuilder time(LocalDateTime startTime);
    }

    public interface GameRefsBuilder { GameBuildFinalizer refs(Referee main, List<Referee> supporting); }

    public interface GameBuildFinalizer {
        GameBuildFinalizer withId(int id);
        Game build();
    }

    public static class GameBuilder implements
            GameTeamsBuilder, GameTimeBuilder, GameRefsBuilder, GameBuildFinalizer {

        private final Game result;

        public GameBuilder() {
            result = new Game();
        }

        @Override
        public GameTimeBuilder teams(Team home, Team away) {
            result.setHome(home);
            result.setAway(away);
            return this;
        }

        @Override
        public GameRefsBuilder time(LocalDateTime startTime, LocalDateTime endTime) {
            result.setStartDate(startTime);
            result.setEndDate(endTime);
            return this;
        }

        @Override
        public GameRefsBuilder time(LocalDateTime startTime) {
            result.setStartDate(startTime);
            result.setEndDate(startTime.plusMinutes(GAME_LENGTH));
            return this;
        }

        @Override
        public GameBuildFinalizer refs(Referee main, List<Referee> supporting) {
            result.setMainRef(main);
            result.setSupportingRefs(supporting);
            return this;
        }

        @Override
        public GameBuildFinalizer withId(int id) {
            result.setId(id);
            return this;
        }

        @Override
        public Game build() {
            return result;
        }
    }

}
