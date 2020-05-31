package com.morbis.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import org.hibernate.type.DateType;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(name = "Fan Registration")
@AllArgsConstructor
public class PlayerRegistrationDTO {

    @Schema(example = "2016-11-09 10:30",
            format = "date-time",
            required = true,
            type = "string")
    public String birthday;

    @Schema(example = "Goalkeeper",
            required = true,
            type = "string")
    public String position;
}
