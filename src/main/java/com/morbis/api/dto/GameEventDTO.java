package com.morbis.api.dto;

import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@Schema(name = "Game Event")
public class GameEventDTO {

    @Schema(
            description = "when creating a new event do not provide an id, when updating do. " +
                    "When the server returns an event it will always provide its id.",
            example = "321")
    public int id;

    @Schema(
            description = "the date and time when the event occurred",
            example = "[2020,5,26,16,49,32,817014100] [year, month, day, hour (24), minute, second, millisecond]",
            required = true)
    public LocalDateTime date;

    @Schema(description = "the in game time when the event occurred (int minutes)",
            minimum = "0",
            maximum = "125",
            example = "90",
            required = true)
    public int gameTime;

    @Schema(example = "very close offside call", required = true)
    public String description;

    @Schema(example = "GOAL", required = true)
    public GameEventType type;

    public GameEvent asGameEvent() {
        return GameEvent.newGameEvent()
                .type(type)
                .time(date, gameTime)
                .description(description)
                .withId(id)
                .build();
    }

    public static GameEventDTO fromGameEvent(GameEvent event) {
        return new GameEventDTO(event.getId(), event.getDate(), event.getGameTime(),
                event.getDescription(), event.getType());
    }
}
