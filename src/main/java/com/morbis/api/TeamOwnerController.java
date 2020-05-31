package com.morbis.api;

import com.fasterxml.jackson.databind.node.TextNode;
import com.morbis.service.TeamOwnerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/team-owner")
public class TeamOwnerController {

    private final TeamOwnerService teamOwnerService;

    public TeamOwnerController(TeamOwnerService teamOwnerService) {
        this.teamOwnerService = teamOwnerService;
    }

    @PostMapping("/{ownerID}/create-team")
    @Operation(summary = "create a new team for the owner")
    public ResponseEntity<?> createTeam(@PathVariable int ownerID,
                                        @Schema(implementation = String.class) @RequestBody TextNode teamName) {

        teamOwnerService.createTeam(ownerID, teamName.asText());
        return ResponseEntity.ok().build();
    }
}
