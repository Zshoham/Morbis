package com.morbis.model.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.morbis.model.team.entity.Team;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamOwner extends Member {

    public TeamOwner(int id, String username, String password, String name, String email, Team team) {
        super(id, MemberRole.TEAM_OWNER, username, password, name, email);
        setTeam(team);
    }

    public TeamOwner(String username, String password, String name, String email, Team team) {
        super(MemberRole.TEAM_OWNER, username, password, name, email);
        setTeam(team);
    }

    @ManyToOne(targetEntity = Team.class)
    @JsonBackReference
    private Team team;

    public static AbstractMemberBuilder<OwnerBuilder> newTeamOwner() {
        return new AbstractMemberBuilder<>(MemberRole.TEAM_OWNER, new OwnerBuilder());
    }

    public static class OwnerBuilder extends MemberBuilder<TeamOwner> {

        private final TeamOwner result;

        public OwnerBuilder() {
            result = new TeamOwner();
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public OwnerBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public OwnerBuilder withTeam(Team team) {
            result.setTeam(team);
            return this;
        }

        @Override
        public TeamOwner build() {
            return result;
        }
    }
}
