package com.morbis.model.member;

import com.morbis.model.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Referee extends Member {
    @NotBlank
    @NotNull
    private String qualification;

    @NotNull
    @OneToMany(targetEntity = Game.class)
    private List<Game> mainGames;

    @NotNull
    @ManyToMany(targetEntity = Game.class)
    private List<Game> supportGames;
}
