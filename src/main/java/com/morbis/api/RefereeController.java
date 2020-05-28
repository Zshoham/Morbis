package com.morbis.api;

import com.morbis.api.dto.GameDTO;
import com.morbis.api.dto.GameEventDTO;
import com.morbis.model.game.entity.Game;
import com.morbis.service.RefereeService;
import com.morbis.service.viewable.MatchReport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/referee")
public class RefereeController {

    private final RefereeService refereeService;

    private final SimpMessagingTemplate eventEmitter;

    public RefereeController(RefereeService refereeService, SimpMessagingTemplate eventEmitter) {
        this.refereeService = refereeService;
        this.eventEmitter = eventEmitter;
    }

    @MessageMapping("/{refID}/game-event/")
    public void updateOnGoingGameEvent(@DestinationVariable int refID,
                                               GameEventDTO event) {

        int gameID = refereeService.getOngoingGame(refID)
                .orElseThrow(() -> new IllegalArgumentException("referee with id=" + refID + " has no ongoing game"))
                .getId();

        if (refereeService.updateOnGoingGameEvent(refID, event.asGameEvent(), gameID))
            eventEmitter.convertAndSend("/api/events/game-events/" + gameID, event);
    }

    @GetMapping("/{refID}/games")
    @Operation(summary = "view all the games the ref is assigned to, past and future")
    public ResponseEntity<List<GameDTO>> getRefGames(@PathVariable int refID) {

        List<GameDTO> res = refereeService.getRefGames(refID).stream()
                .map(GameDTO::fromGame)
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{refID}/game-ongoing")
    public ResponseEntity<?> getOngoingGame(@PathVariable int refID) {
        Optional<Game> game = refereeService.getOngoingGame(refID);
        if (game.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(GameDTO.fromGame(game.get()));
    }

    @GetMapping("/game-events")
    @Operation(summary = "view all the events that have already occurred in a game")
    public ResponseEntity<List<GameEventDTO>> getGameEvents(@RequestParam int gameID) {

        List<GameEventDTO> res = refereeService.getGameEvents(gameID).stream()
                .map(GameEventDTO::fromGameEvent)
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/{refID}/game-events-ongoing")
    @Operation(
            summary = "view all the events that have already occurred in the referees current game",
            description = "note that if the referee is not participating in the game at the moment," +
                    "the server will return an empty list and not an error")
    public ResponseEntity<List<GameEventDTO>> getOnGoingGameEvents(@PathVariable int refID) {

        List<GameEventDTO> res = refereeService.getOnGoingGameEvents(refID).stream()
                .map(GameEventDTO::fromGameEvent)
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    @PostMapping("/{refID}/update-event")
    @Operation(summary = "update a game event after the game is over",
            description = "note that in this case the GameEvent that is sent must " +
                    "contain a valid id in order to update the same event.")
    @ApiResponse(responseCode = "401", description = "either the referee was not the main referee" +
            "in the specified game, or the update period (of 5 hours past the games end) has elapsed.")
    public ResponseEntity<?> updateGameEvent(@PathVariable int refID,
                                             @RequestParam int gameID,
                                             @RequestBody GameEventDTO updated) {

        if (refereeService.updateGameEvent(refID, updated.asGameEvent(), gameID))
            return ResponseEntity.ok().build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/{refID}/match-report")
    public ResponseEntity<MatchReport> getMatchReport(@PathVariable int refID, @RequestParam int gameID) {
        MatchReport report = refereeService.getMatchReport(refID, gameID);
        return ResponseEntity.ok(report);
    }
}
