package com.morbis.api;

import com.morbis.api.dto.LeagueDTO;
import com.morbis.api.dto.SeasonDTO;
import com.morbis.api.dto.TeamRequestDTO;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import com.morbis.model.league.entity.Season;
import com.morbis.model.member.entity.Referee;
import com.morbis.service.AssociationRepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/association-rep")
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


    @PostMapping("/leagues")
    @Operation(summary = "add a league to the system")
    @ApiResponse(responseCode = "200", description = "successfully added a league.")
    @ApiResponse(responseCode = "400", description = "A league with that name is already exist.")
    public ResponseEntity<?> addLeague(@RequestParam String leagueName) {
        if (associationRepService.addLeague(leagueName)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/leagues/{leagueID}/seasons")
    @Operation(summary = "add a season to the system")
    @ApiResponse(responseCode = "200", description = "successfully added a Season.")
    public ResponseEntity<?> addSeason(@PathVariable int leagueID,
                                       @RequestParam int year) {
        associationRepService.addSeason(leagueID, year);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/leagues/{leagueID}/seasons")
    @Operation(summary = "retrieve all the seasons of a specific league")
    @ApiResponse(responseCode = "200", description = "successfully added a Season.")
    public ResponseEntity<List<SeasonDTO>> getSeasons(@PathVariable int leagueID) {
        List<SeasonDTO> res = associationRepService.getSeasons(leagueID).stream()
                .map(SeasonDTO::fromSeason)
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

    @GetMapping("/pending-team-requests")
    @Operation(summary = "get all members requests for open new teams")
    @ApiResponse(responseCode = "200", description = "list of all the requests")
    public ResponseEntity<List<TeamRequestDTO>> getAllPendingRequests() {
        List<TeamRequestDTO> res = associationRepService.getAllPendingRequests()
                .stream().map(TeamRequestDTO::fromRequest).collect(Collectors.toList());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/handle-team-request")
    @Operation(summary = "approve or deny members requests to become a team owner")
    public ResponseEntity<?> handleNewTeamOwnerRequest(@RequestParam int memberID, @RequestParam boolean approved) {
        associationRepService.handleNewTeamOwnerRequest(memberID, approved);
        return ResponseEntity.ok().build();
    }


}
