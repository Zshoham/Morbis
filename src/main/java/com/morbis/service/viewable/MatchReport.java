package com.morbis.service.viewable;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import lombok.Getter;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class MatchReport {

    private final String title;

    private final String referee;

    private final List<String> events;

    public MatchReport(Game game) {
        this.title = Game.getDescription(game);
        this.referee = game.getMainRef().getName();
        this.events = game.getEvents().stream()
                .sorted(Comparator.comparingInt(GameEvent::getGameTime))
                .map(MatchReport::getEventString)
                .collect(Collectors.toUnmodifiableList());
    }

    private static String getEventString(GameEvent event) {
        String gameTime = event.getGameTime() + "'";
        String eventType = event.getType().toString();

        return "[ " + gameTime + " ]" + " - " + eventType + ": " + event.getDescription();
    }
}
