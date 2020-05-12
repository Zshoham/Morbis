package com.morbis.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(name = "Login Info")
@AllArgsConstructor
public class LoginDTO {

    @Schema(example = "user", required = true)
    public String username;
    @Schema(example = "54qew32e", required = true)
    public String password;
}
