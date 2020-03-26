package com.morbis.model.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GameEvent {
    @Id
    @GeneratedValue
    private int id;

    //TODO how to connect enum to DB.
    //private GameEventType type;

    @NotNull
    private LocalDateTime date;

    @PositiveOrZero
    private int gameTime;

    @NotNull
    @NotBlank
    private String description;
}
