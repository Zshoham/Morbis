package com.morbis.model.league.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class League {
    @Id
    @GeneratedValue
    private int id;

    public League(int id, String name) {
        setId(id);
        setName(name);
    }

    public League(String name) {
        setName(name);
    }

    @NotBlank
    @NotNull
    private String name;

    @OneToMany(targetEntity = Season.class, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Season> seasons;

    @Enumerated(EnumType.ORDINAL)
    private ScoringMethod scoringMethod = ScoringMethod.THREE_POINTS_FOR_WIN;

    @Enumerated(EnumType.ORDINAL)
    private SchedulingMethod schedulingMethod = SchedulingMethod.TWO_FIXTURE;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof League)) return false;

        League league = (League) o;

        if (id != league.id) return false;
        return name.equals(league.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }

    public static LeagueBuilder newLeague(String name) {
        return new LeagueBuilder(name);
    }

    public static class LeagueBuilder {

        private final League result;

        public LeagueBuilder(String name) {
            result = new League(name);
        }

        public LeagueBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public LeagueBuilder withSeasons(List<Season> seasons) {
            result.setSeasons(seasons);
            return this;
        }

        public LeagueBuilder withScoringMethod(ScoringMethod scoringMethod) {
            result.setScoringMethod(scoringMethod);
            return this;
        }

        public LeagueBuilder withSchedulingMethod(SchedulingMethod schedulingMethod) {
            result.setSchedulingMethod(schedulingMethod);
            return this;
        }

        public League build() {
            return result;
        }
    }
}
