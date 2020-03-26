package com.morbis.model.league;

import com.morbis.model.game.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Season {
    @Id
    @GeneratedValue
    private int id;

    @Range(min = 1900)
    private int year;

    @OneToMany(targetEntity = Game.class)
    private List<Game> games;


}
