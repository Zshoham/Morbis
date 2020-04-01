package com.morbis.model.member;

import com.morbis.model.team.Team;
import lombok.*;

import javax.persistence.*;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class TeamManager extends Member {
    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    private List<ManagerPermissions> permissions;

    @NotNull
    @ManyToOne(targetEntity = Team.class)
    private Team team;
}
