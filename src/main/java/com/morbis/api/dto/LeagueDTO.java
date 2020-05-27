package com.morbis.api.dto;

import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Schema(name = "league information")
public class LeagueDTO {

    @Schema(example = "553", required = true)
    public int leagueID;
    @Schema(example = "La Liga", required = true)
    public String leagueName;
    @Schema(example = "THREE_POINTS_FOR_WIN", required = true)
    public ScoringMethod scoringMethod;
    @Schema(example = "TWO_FIXTURE", required = true)
    public SchedulingMethod schedulingMethod;

    public static LeagueDTO fromLeague(League league) {
        return new LeagueDTO(
                league.getId(),
                league.getName(),
                league.getScoringMethod(),
                league.getSchedulingMethod());
    }
}
