package com.morbis.model.member.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum MemberRole {
    FAN,
    PLAYER,
    ASSOCIATION_REP,
    ADMIN,
    TEAM_OWNER,
    TEAM_MANAGER,
    REFEREE,
    COACH;

    public static List<ManagerPermissions> canBecomeOwner = Stream.of(
        FAN,
        PLAYER,
            TEAM_MANAGER,
            REFEREE,
            COACH,
    ).collect(Collectors.toList());
}
