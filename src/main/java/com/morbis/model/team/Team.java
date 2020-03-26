package com.morbis.model.team;

import com.morbis.model.member.Coach;
import com.morbis.model.member.Player;
import com.morbis.model.member.TeamManager;
import com.morbis.model.member.TeamOwner;
import com.morbis.model.poster.PosterData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Team {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @OneToMany(targetEntity = Player.class)
    private List<Player> players;

    @NotNull
    @OneToMany(targetEntity = TeamOwner.class)
    private List<TeamOwner> owners;

    @NotNull
    @OneToMany(targetEntity = Coach.class)
    private List<Coach> coaches;

    @NotNull
    @OneToMany(targetEntity = TeamManager.class)
    private List<TeamManager> managers;

    @NotNull
    @OneToOne(targetEntity = PosterData.class)
    private PosterData posterData;

    @NotNull
    @OneToOne(targetEntity = Stadium.class)
    private Stadium stadium;

    @NotNull
    @OneToMany(targetEntity = Transaction.class)
    private List<Transaction> transactions;
}
