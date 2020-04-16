package com.morbis.model.member.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class TeamManager extends Member {

    public TeamManager(int id, String username, String password, String name, String email, List<ManagerPermissions> permissions, Team team) {
        super(id, MemberRole.TEAM_MANAGER, username, password, name, email);
        setPermissions(permissions);
        setTeam(team);
    }

    public TeamManager(String username, String password, String name, String email, List<ManagerPermissions> permissions, Team team) {
        super(MemberRole.TEAM_MANAGER, username, password, name, email);
        setPermissions(permissions);
        setTeam(team);
    }

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ManagerPermissions> permissions;

    @ManyToOne(targetEntity = Team.class)
    @JsonBackReference
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TeamManager)) return false;
        if (!super.equals(o)) return false;

        TeamManager that = (TeamManager) o;

        if (!permissions.equals(that.permissions)) return false;
        return team.equals(that.team);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + permissions.hashCode();
        result = 31 * result + team.hashCode();
        return result;
    }

    public static AbstractMemberBuilder<ManagerBuilder> newTeamManager(List<ManagerPermissions> permissions) {
        return new AbstractMemberBuilder<>(MemberRole.TEAM_MANAGER, new ManagerBuilder(permissions));
    }

    public static class ManagerBuilder extends MemberBuilder<TeamManager> {

        private final TeamManager result;

        public ManagerBuilder(List<ManagerPermissions> permissions) {
            result = new TeamManager();
            result.setPermissions(permissions);
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public ManagerBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public ManagerBuilder withTeam(Team team) {
            result.setTeam(team);
            return this;
        }

        @Override
        public TeamManager build() {
            return result;
        }
    }
}
