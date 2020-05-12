package com.morbis.api.dto;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@Schema(name = "Fan Registration")
@AllArgsConstructor
public class RegisterDTO {

    @Schema(example = "user", required = true)
    public String username;
    @Schema(example = "qwe546", required = true)
    public String password;
    @Schema(example = "dani", required = true)
    public String name;
    @Schema(example = "dani@gmail.com", required = true)
    public String email;

    public Member asMember () {
        return Fan.newFan()
                .fromMember(username, password, name, email)
                .build();
    }
}
