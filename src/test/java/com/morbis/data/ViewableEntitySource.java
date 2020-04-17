package com.morbis.data;

import com.morbis.model.game.entity.Game;
import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.Season;
import com.morbis.model.member.entity.*;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewableEntitySource {

    public static Stadium homeStadium;
    public static Stadium awayStadium;
    public static Player homePlayer;
    public static Player awayPlayer;
    public static TeamOwner homeOwner;
    public static TeamOwner awayOwner;
    public static TeamManager homeManager;
    public static TeamManager awayManager;
    public static Coach homeCoach;
    public static Coach awayCoach;
    public static Team home;
    public static Team away;
    public static Referee main;
    public static Referee supporting;
    public static Game game;
    public static Season season;
    public static League league;


    public static void initWithoutID() {
        homeStadium = Stadium.newStadium("name home")
                .build();

        awayStadium = Stadium.newStadium("name waay")
                .build();

        homePlayer = Player.newPlayer(LocalDateTime.now(), "ST")
                .fromMember("home player", "pass", "name", "email")
                .build();

        awayPlayer = Player.newPlayer(LocalDateTime.now(), "ST")
                .fromMember("away player", "pass", "name", "email")
                .build();

        homeOwner = TeamOwner.newTeamOwner()
                .fromMember("home owner", "pass", "name", "email")
                .build();

        awayOwner = TeamOwner.newTeamOwner()
                .fromMember("away owner", "pass", "name", "email")
                .build();

        homeManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("home manager", "pass", "name", "email")
                .build();

        awayManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("away manager", "pass", "name", "email")
                .build();

        homeCoach = Coach.newCoach("school", "attack")
                .fromMember("home coach", "pass", "name", "email")
                .build();

        awayCoach = Coach.newCoach("school", "attack")
                .fromMember("away coach", "pass", "name", "email")
                .build();

        home = Team.newTeam()
                .name("name")
                .players(Collections.singletonList(homePlayer))
                .owners(Collections.singletonList(homeOwner))
                .coaches(Collections.singletonList(homeCoach))
                .stadium(homeStadium)
                .withManagers(Collections.singletonList(homeManager))
                .build();

        away = Team.newTeam()
                .name("name")
                .players(Collections.singletonList(awayPlayer))
                .owners(Collections.singletonList(awayOwner))
                .coaches(Collections.singletonList(awayCoach))
                .stadium(awayStadium)
                .withManagers(Collections.singletonList(awayManager))
                .build();

        main = Referee.newReferee("school")
                .fromMember("main", "pass", "name", "email")
                .build();

        supporting = Referee.newReferee("school")
                .fromMember("supporting", "pass", "name", "email")
                .build();

        game = Game.newGame()
                .teams(home, away)
                .time(LocalDateTime.now())
                .refs(main, Collections.singletonList(supporting))
                .build();


        league = League.newLeague("name")
                .build();

        season = Season.newSeason(2020, league)
                .withGames(Collections.singletonList(game))
                .build();

        createALlLinks();
    }

    public static void initWithID() {
        homeStadium = Stadium.newStadium("name home")
                .withId(1)
                .build();

        awayStadium = Stadium.newStadium("name away")
                .withId(2)
                .build();

        homePlayer = Player.newPlayer(LocalDateTime.now(), "ST")
                .fromMember("home player", "pass", "name", "email")
                .withId(3)
                .build();

        awayPlayer = Player.newPlayer(LocalDateTime.now(), "ST")
                .fromMember("away player", "pass", "name", "email")
                .withId(4)
                .build();

        homeOwner = TeamOwner.newTeamOwner()
                .fromMember("home owner", "pass", "name", "email")
                .withId(5)
                .build();

        awayOwner = TeamOwner.newTeamOwner()
                .fromMember("away owner", "pass", "name", "email")
                .withId(6)
                .build();

        homeManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("home manager", "pass", "name", "email")
                .withId(7)
                .build();

        awayManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("away manager", "pass", "name", "email")
                .withId(8)
                .build();

        homeCoach = Coach.newCoach("school", "attack")
                .fromMember("home coach", "pass", "name", "email")
                .withId(9)
                .build();

        awayCoach = Coach.newCoach("school", "attack")
                .fromMember("away coach", "pass", "name", "email")
                .withId(10)
                .build();

        home = Team.newTeam()
                .name("name")
                .players(Collections.singletonList(homePlayer))
                .owners(Collections.singletonList(homeOwner))
                .coaches(Collections.singletonList(homeCoach))
                .stadium(homeStadium)
                .withManagers(Collections.singletonList(homeManager))
                .withId(11)
                .build();

        away = Team.newTeam()
                .name("name")
                .players(Collections.singletonList(awayPlayer))
                .owners(Collections.singletonList(awayOwner))
                .coaches(Collections.singletonList(awayCoach))
                .stadium(awayStadium)
                .withManagers(Collections.singletonList(awayManager))
                .withId(12)
                .build();

        main = Referee.newReferee("school")
                .fromMember("main", "pass", "name", "email")
                .withId(13)
                .build();

        supporting = Referee.newReferee("school")
                .fromMember("supporting", "pass", "name", "email")
                .withId(14)
                .build();

        game = Game.newGame()
                .teams(home, away)
                .time(LocalDateTime.now())
                .refs(main, Collections.singletonList(supporting))
                .withId(15)
                .build();


        league = League.newLeague("name")
                .withId(17)
                .build();

        season = Season.newSeason(2020, league)
                .withGames(Collections.singletonList(game))
                .withId(16)
                .build();

        createALlLinks();
    }

    private static void createALlLinks() {
        homeStadium.setTeam(home);
        awayStadium.setTeam(away);

        homeCoach.setTeam(home);
        awayCoach.setTeam(away);

        homeManager.setTeam(home);
        awayManager.setTeam(away);

        homeOwner.setTeam(home);
        awayOwner.setTeam(away);

        homePlayer.setTeam(home);
        awayPlayer.setTeam(away);

        main.setMainGames(Collections.singletonList(game));
        supporting.setSupportGames(Collections.singletonList(game));

        league.setSeasons(Collections.singletonList(season));
    }

}
