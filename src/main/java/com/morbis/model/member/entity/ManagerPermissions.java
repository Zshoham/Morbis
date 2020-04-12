package com.morbis.model.member.entity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ManagerPermissions {

    GET_ASSETS,
    GET_ASSETS_TEAM_ID,
    ADD_ASSETS,
    REMOVE_ASSETS,
    UPDATE_ASSETS,
    GET_NON_OWNERS,
    MAKE_TEAM_OWNER,
    GET_APPOINTED_OWNERS,
    REMOVE_OWNERS,
    GET_NON_MANAGERS,
    MAKE_TEAM_MANAGER,
    GET_APPOINTED_MANAGERS,
    REMOVE_MANAGERS,
    CLOSE_TEAM,
    OPEN_TEAM,
    SET_PERMISSIONS,
    SUBMIT_TRANSACTION;


    public static List<ManagerPermissions> all = Stream.of(
            GET_ASSETS,
            GET_ASSETS_TEAM_ID,
            ADD_ASSETS,
            REMOVE_ASSETS,
            UPDATE_ASSETS,
            GET_NON_OWNERS,
            MAKE_TEAM_OWNER,
            GET_APPOINTED_OWNERS,
            REMOVE_OWNERS,
            GET_NON_MANAGERS,
            MAKE_TEAM_MANAGER,
            GET_APPOINTED_MANAGERS,
            REMOVE_MANAGERS,
            CLOSE_TEAM,
            OPEN_TEAM,
            SET_PERMISSIONS,
            SUBMIT_TRANSACTION
    ).collect(Collectors.toList());
}
