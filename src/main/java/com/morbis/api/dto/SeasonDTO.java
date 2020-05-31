package com.morbis.api.dto;

import com.morbis.model.league.entity.Season;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SeasonDTO {
    @Schema(example = "156",
            required = true,
            type = "integer")
    public int seasonID;

    @Schema(example = "2020",
            required = true,
            type = "integer")
    public int year;


    public static SeasonDTO fromSeason(Season season){
        return new SeasonDTO(season.getId(),season.getYear());
    }
}
