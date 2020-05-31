package com.morbis.api.dto;

import com.morbis.model.member.entity.TeamOwnerRegRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(name = "new team request")
@AllArgsConstructor
public class TeamRequestDTO {
    @Schema(example = "993", description = "the id of the member that made the request also serves as the requests id")
    public int requestingMemberID;

    @Schema(example = "Monte Rio FC")
    public String requestedTeamName;

    public static TeamRequestDTO fromRequest(TeamOwnerRegRequest request){
        return new TeamRequestDTO(request.getId(), request.getRequestingMember().getName());
    }
}
