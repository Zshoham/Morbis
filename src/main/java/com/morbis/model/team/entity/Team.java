package com.morbis.model.team.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.morbis.model.member.entity.Coach;
import com.morbis.model.member.entity.Player;
import com.morbis.model.member.entity.TeamManager;
import com.morbis.model.member.entity.TeamOwner;
import com.morbis.model.poster.entity.PosterData;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue
    private int id;

    private Team(int id, String name, List<Player> players, List<TeamOwner> owners, List<TeamManager> managers, List<Coach> coaches, Stadium stadium) {
        setId(id);
        setName(name);
        setPlayers(players);
        setOwners(owners);
        setManagers(managers);
        setCoaches(coaches);
        setStadium(stadium);
        setTeamStatus(TeamStatus.OPENED);
    }

    public Team(String name, List<Player> players, List<TeamOwner> owners, List<TeamManager> managers, List<Coach> coaches, Stadium stadium) {
        setName(name);
        setPlayers(players);
        setOwners(owners);
        setManagers(managers);
        setCoaches(coaches);
        setStadium(stadium);
        setTeamStatus(TeamStatus.OPENED);
    }

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @OneToMany(targetEntity = Player.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Player> players;

    @NotNull
    @OneToMany(targetEntity = TeamOwner.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TeamOwner> owners;

    @NotNull
    @OneToMany(targetEntity = Coach.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Coach> coaches;

    @OneToMany(targetEntity = TeamManager.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<TeamManager> managers;

    @OneToOne(targetEntity = PosterData.class, cascade = CascadeType.ALL)
    private PosterData posterData;

    @NotNull
    @OneToOne(targetEntity = Stadium.class, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Stadium stadium;

    @OneToMany(targetEntity = Transaction.class)
    private List<Transaction> transactions;

    @NotNull
    private TeamStatus teamStatus;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Team)) return false;

        Team team = (Team) o;

        if (id != team.id) return false;
        if (!name.equals(team.name)) return false;
        if (!teamStatus.equals(team.teamStatus)) return false;
        return stadium.equals(team.stadium);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + stadium.hashCode();
        result = 31 * result + teamStatus.hashCode();
        return result;
    }

    public static TeamNameBuilder newTeam() {
        return new BaseTeamBuilder();
    }

    public interface TeamNameBuilder { TeamPlayersBuilder name(String name); }

    public interface TeamPlayersBuilder { TeamOwnersBuilder players(List<Player> players); }

    public interface TeamOwnersBuilder { TeamCoachesBuilder owners(List<TeamOwner> owners); }

    public interface TeamCoachesBuilder { TeamStadiumBuilder coaches(List<Coach> coaches); }

    public interface TeamStadiumBuilder { TeamBuildFinalizer stadium(Stadium stadium); }

    public interface TeamBuildFinalizer {
        TeamBuildFinalizer withId(int id);
        TeamBuildFinalizer withManagers(List<TeamManager> managers);
        TeamBuildFinalizer withPosterData(PosterData posterData);
        TeamBuildFinalizer withTransactions(List<Transaction> transactions);
        TeamBuildFinalizer withTeamStatus(TeamStatus teamStatus);
        Team build();
    }

    public static class BaseTeamBuilder implements
            TeamNameBuilder, TeamPlayersBuilder, TeamOwnersBuilder, TeamCoachesBuilder, TeamStadiumBuilder,TeamBuildFinalizer {

        private final Team result;

        private BaseTeamBuilder() {
            this.result = new Team();
            this.result.setTeamStatus(TeamStatus.OPENED);
        }

        @Override
        public TeamPlayersBuilder name(String name) {
            result.setName(name);
            return this;
        }

        @Override
        public TeamOwnersBuilder players(List<Player> players) {
            result.setPlayers(players);
            return this;
        }

        @Override
        public TeamCoachesBuilder owners(List<TeamOwner> owners) {
            result.setOwners(owners);
            return this;
        }

        @Override
        public TeamStadiumBuilder coaches(List<Coach> coaches) {
            result.setCoaches(coaches);
            return this;
        }

        @Override
        public TeamBuildFinalizer stadium(Stadium stadium) {
            result.setStadium(stadium);
            return this;
        }

        @Override
        public TeamBuildFinalizer withId(int id) {
            result.setId(id);
            return this;
        }

        @Override
        public TeamBuildFinalizer withManagers(List<TeamManager> managers) {
            result.setManagers(managers);
            return this;
        }

        @Override
        public TeamBuildFinalizer withPosterData(PosterData posterData) {
            result.setPosterData(posterData);
            return this;
        }

        @Override
        public TeamBuildFinalizer withTransactions(List<Transaction> transactions) {
            result.setTransactions(transactions);
            return this;
        }

        @Override
        public TeamBuildFinalizer withTeamStatus(TeamStatus teamStatus) {
            result.setTeamStatus(teamStatus);
            return this;
        }

        @Override
        public Team build() { return result; }
    }
}
