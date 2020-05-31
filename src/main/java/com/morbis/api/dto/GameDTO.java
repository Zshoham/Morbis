package com.morbis.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morbis.model.game.entity.Game;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Schema(name = "Basic Game Information")
public class GameDTO {

    @Schema(example = "968", required = true)
    public int id;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm a")
    @Schema(format = "dd.mm.yyyy hh:mm AM/PM", example = "22.3.2003 16:00 PM", required = true)
    public LocalDateTime startDate;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm a")
    @Schema(format = "dd.mm.yyyy hh:mm AM/PM", example = "22.3.2003 17:30 PM", required = true)
    public LocalDateTime endDate;

    @Schema(format = "{home team name} V {away team name}", example = "real madrid V barcelona", required = true)
    public String description;


    public static GameDTO fromGame(Game game) {
        return new GameDTO(
                game.getId(),
                game.getStartDate(),
                game.getEndDate(),
                Game.getDescription(game));
    }
}
