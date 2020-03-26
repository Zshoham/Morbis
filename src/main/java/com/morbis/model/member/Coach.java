package com.morbis.model.member;

import com.morbis.model.poster.PosterData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Coach extends Member {
    @NotBlank
    @NotNull
    private String qualification;

    @NotBlank
    @NotNull
    private String role;

    @OneToOne(targetEntity = PosterData.class)
    private PosterData posterData;
}
