package com.morbis.model.member;

import com.morbis.model.poster.PosterData;
import com.morbis.model.team.Team;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
