package com.morbis.model.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Inheritance(strategy =InheritanceType.JOINED)
public abstract class Member {
    @Id
    @GeneratedValue
    protected int id;

    @NotNull
    @NotBlank
    protected String username;

    @NotNull
    @NotBlank
    protected String password;

    @NotNull
    @NotBlank
    protected String name;

    @NotNull
    @NotBlank
    protected String email;
}
