package com.morbis.model.game.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.Objects;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class GameEvent {
    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(targetEntity = Game.class)
    private Game game;

    public GameEvent(int id, GameEventType type, LocalDateTime date, int gameTime, String description) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.gameTime = gameTime;
        this.description = description;
    }


    public GameEvent(GameEventType type, LocalDateTime date, int gameTime, String description) {
        this.type = type;
        this.date = date;
        this.gameTime = gameTime;
        this.description = description;
    }

    @Enumerated(EnumType.ORDINAL)
    private GameEventType type;

    @NotNull
    private LocalDateTime date;

    @PositiveOrZero
    private int gameTime;

    @NotNull
    @NotBlank
    private String description;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameEvent)) return false;

        GameEvent gameEvent = (GameEvent) o;

        if (id != gameEvent.id) return false;
        if (gameTime != gameEvent.gameTime) return false;
        if (type != gameEvent.type) return false;
        if (!Objects.equals(date, gameEvent.date)) return false;
        return Objects.equals(description, gameEvent.description);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + gameTime;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    public static EventTypeBuilder newGameEvent() {
        return new GameEventBuilder();
    }

    public interface EventTypeBuilder { EventTimeBuilder type(GameEventType type); }

    public interface EventTimeBuilder { EventDescriptionBuilder time(LocalDateTime date, int gameTime); }

    public interface EventDescriptionBuilder { EventBuildFinalizer description(String description); }

    public interface EventBuildFinalizer {
        EventBuildFinalizer withId(int id);
        GameEvent build();
    }

    public static class GameEventBuilder implements
            EventTypeBuilder, EventTimeBuilder, EventDescriptionBuilder, EventBuildFinalizer {

        private final GameEvent result;


        public GameEventBuilder() {
            this.result = new GameEvent();
        }

        @Override
        public EventTimeBuilder type(GameEventType type) {
            result.setType(type);
            return this;
        }

        @Override
        public EventDescriptionBuilder time(LocalDateTime date, int gameTime) {
            result.setDate(date);
            result.setGameTime(gameTime);
            return this;
        }

        @Override
        public EventBuildFinalizer description(String description) {
            result.setDescription(description);
            return this;
        }

        @Override
        public EventBuildFinalizer withId(int id) {
            result.setId(id);
            return this;
        }

        @Override
        public GameEvent build() {
            return result;
        }
    }

}
