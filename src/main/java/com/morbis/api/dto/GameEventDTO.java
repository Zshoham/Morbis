package com.morbis.api.dto;

import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.entity.GameEventType;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class GameEventDTO {

    public LocalDateTime date;
    public int gameTime;
    public String description;
    public GameEventType type;

    public GameEvent asGameEvent() {
        return GameEvent.newGameEvent()
                .type(type)
                .time(date, gameTime)
                .description(description)
                .build();
    }
}
