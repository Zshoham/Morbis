package com.morbis.model.game;

import com.morbis.model.member.Referee;
import com.morbis.model.team.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


//TODO Need to understand how to do inheritance in DB.

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Game {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne(targetEntity = Team.class)
    private Team home;

    @NotNull
    @ManyToOne(targetEntity = Team.class)
    private Team away;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @OneToMany(targetEntity = GameEvent.class)
    private List<GameEvent> events;

    @ManyToOne(targetEntity = Referee.class)
    private Referee mainRef;

    @ManyToMany(targetEntity = Referee.class)
    private List<Referee> supportingRefs;
}
