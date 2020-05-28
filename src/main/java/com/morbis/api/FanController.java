package com.morbis.api;

import com.fasterxml.jackson.databind.node.TextNode;
import com.morbis.api.dto.CoachRegistrationDTO;
import com.morbis.api.dto.GameDTO;
import com.morbis.api.dto.PlayerRegistrationDTO;
import com.morbis.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/fan")
public class FanController {
    private final MemberService memberService;

    public FanController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/{memberID}/registerAsCoach")
    @Operation(summary = "register member as a coach")
    @ApiResponse(responseCode = "200", description = "successfully registered as a coach")
    @ApiResponse(responseCode = "400", description = "member is already a coach")
    public ResponseEntity<?> registerAsCoach(@PathVariable int memberID,
                                             @RequestBody CoachRegistrationDTO addCoach)  {
        memberService.registerAsCoach(memberID, addCoach.qualification, addCoach.role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{memberID}/registerAsPlayer")
    @Operation(summary = "register member as a player")
    @ApiResponse(responseCode = "200", description = "successfully registered as a player")
    @ApiResponse(responseCode = "400", description = "member is already a player")
    public ResponseEntity<?> registerAsPlayer(@PathVariable int memberID,
                                              @RequestBody PlayerRegistrationDTO addPlayer)  {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate birthDate = LocalDate.parse(addPlayer.birthday, formatter);
        memberService.registerAsPlayer(memberID, birthDate,addPlayer.position);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{memberID}/requestRegisterAsTeamOwner")
    @Operation(summary = "register member as a Team Owner")
    @ApiResponse(responseCode = "200", description = "successfully registered as a Team Owner")
    @ApiResponse(responseCode = "400", description = "member is already a team owner or team with this name already exist")
    public ResponseEntity<?> requestRegisterAsTeamOwner(@PathVariable int memberID,
                                                        @Parameter(schema = @Schema(implementation = String.class)) @RequestBody TextNode teamName)  {
        memberService.requestRegisterAsTeamOwner(memberID, teamName.asText());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{memberID}/followGame")
    @Operation(summary = "follow a game")
    @ApiResponse(responseCode = "200", description = "successfully followed game")
    @ApiResponse(responseCode = "400", description = "member or game not found")
    public ResponseEntity<?> followGame(@PathVariable int memberID,
                                        @RequestParam int gameID){
        memberService.followGame(memberID, gameID);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{memberID}/gamesFollowing")
    @Operation(summary = "retrieve the games that the fan is following")
    @ApiResponse(responseCode = "404", description = "no games were found in the fans follow list")
    public ResponseEntity<?> getGamesFollowing(@PathVariable int memberID) {
        List<GameDTO> res = memberService.getGamesFollowing(memberID).stream()
                .map(GameDTO::fromGame)
                .collect(Collectors.toList());

        if (res.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(res);
    }

}

