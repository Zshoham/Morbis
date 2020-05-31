package com.morbis.api.dto;

import com.morbis.model.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

import java.util.List;

@Schema(name = "Login response body")
@AllArgsConstructor
public class LoginResponseDTO {
    @Schema(example = "43217", required = true)
    public String token;
    @Schema(example = "FAN, PLAYER, COACH", required = true)
    public List<MemberRole> roles;
    @Schema(example = "1856", required = true)
    public int memberID;
}
