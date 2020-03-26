package com.morbis.model.member;

import com.morbis.model.poster.PosterData;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
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
