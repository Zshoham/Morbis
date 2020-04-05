package com.morbis.model.league.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class League {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    @NotNull
    private String name;

    @OneToMany(targetEntity = Season.class)
    private List<Season> seasons;
}
