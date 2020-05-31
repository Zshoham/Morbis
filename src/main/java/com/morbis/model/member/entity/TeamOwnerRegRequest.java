package com.morbis.model.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class TeamOwnerRegRequest {
    @Id
    private int id;

    @OneToOne(targetEntity = Member.class)
    private Member requestingMember;

    public TeamOwnerRegRequest(Member requestingMember){
        this.id = requestingMember.getId();
        this.requestingMember = requestingMember;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TeamOwnerRegRequest that = (TeamOwnerRegRequest) o;
        return id == that.id &&
                Objects.equals(requestingMember, that.requestingMember);
    }
}