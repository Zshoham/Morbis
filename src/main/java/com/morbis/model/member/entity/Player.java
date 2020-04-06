package com.morbis.model.member.entity;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Player extends Member {
    @NotNull
    private LocalDateTime birthDate;

    @NotNull
    @NotBlank
    private String position;

    @OneToOne(targetEntity = PosterData.class)
    private PosterData posterData;

    @ManyToOne(targetEntity = Team.class)
    private Team team;
}
