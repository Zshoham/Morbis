package com.morbis.model.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


//TODO Need to understand how to do inheritance in DB.

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
public class Member {
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
