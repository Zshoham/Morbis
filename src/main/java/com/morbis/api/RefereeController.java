package com.morbis.api;

import com.morbis.api.dto.GameEventDTO;
import com.morbis.service.RefereeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/referee")
public class RefereeController {

    private final RefereeService refereeService;

    public RefereeController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }


    @MessageMapping("/game-event/{refID}/{gameID}")
    @SendTo("/api/events/game-events/{gameID}")
    public GameEventDTO updateOnGoingGameEvent(@DestinationVariable int refID,
                                               GameEventDTO event,
                                               @DestinationVariable int gameID) {

        refereeService.updateOnGoingGameEvent(refID, event.asGameEvent(), gameID);
        return event;
    }
}
