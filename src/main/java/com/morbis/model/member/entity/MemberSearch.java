package com.morbis.model.member.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberSearch)) return false;

        MemberSearch other = (MemberSearch) o;

        if (id != other.id) return false;
        if (!Objects.equals(query, other.query)) return false;
        return member.equals(other.member);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (query != null ? query.hashCode() : 0);
        result = 31 * result + member.hashCode();
        return result;
    }

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
