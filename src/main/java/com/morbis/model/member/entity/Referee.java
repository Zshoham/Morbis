package com.morbis.model.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.morbis.model.game.entity.Game;
import com.morbis.model.league.entity.Season;
import com.morbis.model.team.entity.Team;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Referee extends Member {

    public Referee(int id, String username, String password, String name, String email, String qualification) {
        super(id, MemberRole.REFEREE, username, password, name, email);
        setQualification(qualification);
    }

    public Referee(String username, String password, String name, String email, String qualification) {
        super(MemberRole.REFEREE, username, password, name, email);
        setQualification(qualification);
    }

    @NotBlank
    @NotNull
    private String qualification;

    @OneToMany(targetEntity = Game.class, cascade = CascadeType.MERGE)
    @JsonBackReference
    private List<Game> mainGames;

    @ManyToMany(targetEntity = Game.class, cascade = CascadeType.MERGE)
    @JsonBackReference
    private List<Game> supportGames;

    @ManyToMany(targetEntity = Season.class)
    @JsonBackReference
    private List<Season> seasons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Referee)) return false;
        if (!super.equals(o)) return false;

        Referee referee = (Referee) o;

        return qualification.equals(referee.qualification);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + qualification.hashCode();
        return result;
    }




    public static AbstractMemberBuilder<RefereeBuilder> newReferee(String qualification) {
        return new AbstractMemberBuilder<>(MemberRole.REFEREE, new RefereeBuilder(qualification));
    }

    public static class RefereeBuilder extends MemberBuilder<Referee> {

        private final Referee result;

        public RefereeBuilder(String qualification) {
            result = new Referee();
            result.setQualification(qualification);
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public RefereeBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public RefereeBuilder withMainGames(List<Game> mainGames) {
            result.setMainGames(mainGames);
            return this;
        }

        public RefereeBuilder withSupportingGames(List<Game> supportingGames) {
            result.setSupportGames(supportingGames);
            return this;
        }

        public RefereeBuilder withSeasons(List<Season> seasons) {
            result.setSeasons(seasons);
            return this;
        }

        @Override
        public Referee build() {
            return result;
        }
    }
}
