package com.morbis.api;

import com.morbis.api.dto.LeagueDTO;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import com.morbis.service.AssociationRepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/rep")
public class AssociationRepController {

    private final AssociationRepService associationRepService;

    public AssociationRepController(AssociationRepService associationRepService) {
        this.associationRepService = associationRepService;
    }

    @GetMapping("/leagues")
    @Operation(summary = "retrieve all the leagues in the system")
    public ResponseEntity<List<LeagueDTO>> getLeagues() {

        List<LeagueDTO> res = associationRepService.getLeagues().stream()
                .map(LeagueDTO::fromLeague)
                .collect(Collectors.toList());

        return ResponseEntity.ok(res);
    }

    @GetMapping("/scoring-methods")
    @Operation(summary = "retrieve all the possible scoring methods")
    public ResponseEntity<List<ScoringMethod>> getScoringMethods() {
        return ResponseEntity.ok(associationRepService.getScoringMethods());
    }

    @GetMapping("/scheduling-methods")
    @Operation(summary = "retrieve all the possible scheduling methods")
    public ResponseEntity<List<SchedulingMethod>> getSchedulingMethods() {
        return ResponseEntity.ok(associationRepService.getSchedulingMethods());
    }

    @PostMapping("/update-policy")
    @Operation(
            summary = "update the scoring and scheduling methods of the league",
            description = "note that even though the endpoint accepts a league, " +
                    "only the scoring method will be updated event if other data has changed.")
    @ApiResponse(responseCode = "202", description = "new scheduling and scoring methods were accepted.")
    public ResponseEntity<?> updatePolicy(@RequestBody LeagueDTO updated) {
        associationRepService.setScoringMethod(updated.leagueID, updated.scoringMethod);
        associationRepService.setSchedulingMethod(updated.leagueID, updated.schedulingMethod);

        return ResponseEntity.accepted().build();
    }
}
