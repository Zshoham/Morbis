package com.morbis.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(name = "Fan Registration")
@AllArgsConstructor
public class CoachRegistrationDTO {

    @Schema(example = "have a long time experience",
            required = true,
            type = "string")
    public String qualification;

    @Schema(example = "main",
            required = true,
            type = "string")
    public String role;
}
