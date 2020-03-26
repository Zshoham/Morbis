package com.morbis.model.member;

import com.morbis.model.team.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeamOwner extends Member {
    @NotNull
    @ManyToOne(targetEntity = Team.class)
    private Team team;
}
