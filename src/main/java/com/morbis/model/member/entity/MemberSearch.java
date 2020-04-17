package com.morbis.model.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberSearch {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    @NotBlank
    private String query;

    @ManyToOne(targetEntity = Member.class)
    private Member member;

    public MemberSearch(int id, String query, Member member) {
        this.id = id;
        this.query = query;
        this.member = member;
    }

    public MemberSearch(String query, Member member) {
        this.query = query;
        this.member = member;
    }
}
