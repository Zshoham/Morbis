package com.morbis.model.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.morbis.model.poster.entity.PosterData;
import com.morbis.model.team.entity.Team;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Player extends Member {

    public Player(String username, String password, String name, String email, LocalDateTime birthDate, String position) {
        super(MemberRole.PLAYER, username, password, name, email);
        setBirthDate(birthDate);
        setPosition(position);
    }

    public Player(int id, String username, String password, String name, String email, LocalDateTime birthDate, String position) {
        super(id, MemberRole.PLAYER, username, password, name, email);
        setBirthDate(birthDate);
        setPosition(position);
    }

    @NotNull
    private LocalDateTime birthDate;

    @NotNull
    @NotBlank
    private String position;

    @OneToOne(targetEntity = PosterData.class)
    private PosterData posterData;

    @ManyToOne(targetEntity = Team.class)
    @JsonBackReference
    private Team team;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;
        if (!super.equals(o)) return false;

        Player player = (Player) o;

        if (!birthDate.equals(player.birthDate)) return false;
        return position.equals(player.position);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + birthDate.hashCode();
        result = 31 * result + position.hashCode();
        return result;
    }

    public static AbstractMemberBuilder<PlayerBuilder> newPlayer(LocalDateTime birthDate, String position) {
        return new AbstractMemberBuilder<>(MemberRole.PLAYER, new PlayerBuilder(birthDate, position));
    }

    public static class PlayerBuilder extends MemberBuilder<Player> {

        private final Player result;

        public PlayerBuilder(LocalDateTime birthDate, String position) {
            result = new Player();
            result.setBirthDate(birthDate);
            result.setPosition(position);
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public PlayerBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public PlayerBuilder withPosterData(PosterData posterData) {
            result.setPosterData(posterData);
            return this;
        }

        public PlayerBuilder withTeam(Team team) {
            result.setTeam(team);
            return this;
        }

        @Override
        public Player build() {
            return result;
        }
    }
}
