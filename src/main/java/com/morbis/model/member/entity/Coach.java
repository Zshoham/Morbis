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

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Coach extends Member {

    public Coach(int id, String username, String password, String name, String email, String qualification, String role) {
        super(id, MemberRole.COACH, username, password, name, email);
        setQualification(qualification);
        setRole(role);
    }

    public Coach(String username, String password, String name, String email, String qualification, String role) {
        super(MemberRole.COACH, username, password, name, email);
        setQualification(qualification);
        setRole(role);
    }

    @NotBlank
    @NotNull
    private String qualification;

    @NotBlank
    @NotNull
    private String role;

    @OneToOne(targetEntity = PosterData.class)
    private PosterData posterData;

    @ManyToOne(targetEntity = Team.class)
    @JsonBackReference
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coach)) return false;
        if (!super.equals(o)) return false;

        Coach coach = (Coach) o;

        if (!qualification.equals(coach.qualification)) return false;
        return role.equals(coach.role);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + qualification.hashCode();
        result = 31 * result + role.hashCode();
        return result;
    }

    public static AbstractMemberBuilder<CoachBuilder> newCoach(String qualification, String role) {
        return new AbstractMemberBuilder<>(MemberRole.COACH, new CoachBuilder(qualification, role));
    }

    public static class CoachBuilder extends MemberBuilder<Coach> {

        private final Coach result;

        public CoachBuilder(String qualification, String role) {
            result = new Coach();
            result.setQualification(qualification);
            result.setRole(role);
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public CoachBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public CoachBuilder withPosterData(PosterData posterData) {
            result.setPosterData(posterData);
            return this;
        }

        public CoachBuilder withTeam(Team team) {
            result.setTeam(team);
            return this;
        }

        @Override
        public Coach build() {
            return result;
        }
    }
}
