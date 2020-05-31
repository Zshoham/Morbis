package com.morbis.model.team.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Stadium {
    @Id
    @GeneratedValue
    private int id;

    public Stadium(int id, String name) {
        setId(id);
        setName(name);
    }

    public Stadium(String name) { setName(name); }

    @NotNull
    @NotBlank
    private String name;

    @OneToOne(targetEntity = Team.class)
    @JsonBackReference
    private Team team;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stadium)) return false;

        Stadium stadium = (Stadium) o;

        if (id != stadium.id) return false;
        return name.equals(stadium.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    public static StadiumBuilder newStadium(String name) {
        return new StadiumBuilder(name);
    }

    public static class StadiumBuilder {

        private final Stadium result;

        public StadiumBuilder(String name) {
            result = new Stadium(name);
        }

        public StadiumBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public StadiumBuilder withTeam(Team team) {
            result.setTeam(team);
            return this;
        }

        public Stadium build() {
            return result;
        }
    }
}
