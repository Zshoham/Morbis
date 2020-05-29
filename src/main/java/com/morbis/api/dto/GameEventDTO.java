package com.morbis.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@AllArgsConstructor
@Schema(name = "Game Event")
public class GameEventDTO {

    @Schema(
            description = "when creating a new event do not provide an id, when updating do. " +
                    "When the server returns an event it will always provide its id.",
            example = "321")
    public int id;

    @Schema(
            description = "the time the event occurred",
            example = "1590787470115",
            format = "UNIX time in milliseconds, easily convertible to other date and time representations")
    public long date;

    @Schema(description = "the in game time when the event occurred (int minutes)",
            minimum = "0",
            maximum = "125",
            example = "90")
    public int gameTime;

    @Schema(example = "very close offside call", required = true)
    public String description;

    @Schema(example = "GOAL", required = true)
    public GameEventType type;

    public GameEventDTO() {
        this.gameTime = -1;
        this.date = System.currentTimeMillis();
    }

    public GameEvent asGameEvent() {
        return GameEvent.newGameEvent()
                .type(type)
                .time(Instant.ofEpochMilli(date).atZone(ZoneId.systemDefault()).toLocalDateTime(), gameTime)
                .description(description)
                .withId(id)
                .build();
    }

    public static GameEventDTO fromGameEvent(GameEvent event) {
        return new GameEventDTO(event.getId(), event.getDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(), event.getGameTime(),
                event.getDescription(), event.getType());
    }
}
