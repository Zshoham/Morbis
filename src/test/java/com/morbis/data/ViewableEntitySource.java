package com.morbis.data;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.Season;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;

import java.time.LocalDateTime;

import static com.morbis.TestUtils.listOf;

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
        baseInit();
        createALlLinks();
    }

    public static void initWithID() {
        baseInit();
        addIds();
        createALlLinks();
    }

    private static void baseInit() {
        homeStadium = Stadium.newStadium("home stadium")
                .build();

        awayStadium = Stadium.newStadium("away stadium")
                .build();

        homePlayer = Player.newPlayer(LocalDateTime.now(), "ST")
                .fromMember("home player", "pass", "name", "email")
                .build();

        awayPlayer = Player.newPlayer(LocalDateTime.now(), "ST")
                .fromMember("away player", "pass", "name", "email")
                .build();

        homeOwner = TeamOwner.newTeamOwner()
                .fromMember("home owner", "pass", "home name", "email")
                .build();

        awayOwner = TeamOwner.newTeamOwner()
                .fromMember("away owner", "pass", "away name", "email")
                .build();

        homeManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("home manager", "pass", "name", "email")
                .build();

        awayManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("away manager", "pass", "name", "email")
                .build();

        homeCoach = Coach.newCoach("school", "attack")
                .fromMember("home coach", "pass", "home name", "email")
                .build();

        awayCoach = Coach.newCoach("school", "attack")
                .fromMember("away coach", "pass", "away name", "email")
                .build();

        home = Team.newTeam()
                .name("home team")
                .players(listOf(homePlayer))
                .owners(listOf(homeOwner))
                .coaches(listOf(homeCoach))
                .stadium(homeStadium)
                .withManagers(listOf(homeManager))
                .build();

        away = Team.newTeam()
                .name("away team")
                .players(listOf(awayPlayer))
                .owners(listOf(awayOwner))
                .coaches(listOf(awayCoach))
                .stadium(awayStadium)
                .withManagers(listOf(awayManager))
                .build();

        main = Referee.newReferee("school")
                .fromMember("main", "pass", "main name", "email")
                .build();

        supporting = Referee.newReferee("school")
                .fromMember("supporting", "pass", "supporting name", "email")
                .build();

        league = League.newLeague("name")
                .build();

        season = Season.newSeason(2020, league)
                .build();

        game = Game.newGame()
                .teams(home, away)
                .time(LocalDateTime.now())
                .refs(main, listOf(supporting))
                .build();
    }

    private static void addIds() {
        homeStadium.setId(1);
        awayStadium.setId(2);
        homePlayer.setId(3);
        awayPlayer.setId(4);
        homeOwner.setId(5);
        awayOwner.setId(6);
        homeManager.setId(7);
        awayManager.setId(8);
        homeCoach.setId(9);
        awayCoach.setId(10);
        home.setId(11);
        away.setId(12);
        main.setId(13);
        supporting.setId(14);
        league.setId(15);
        season.setId(16);
        game.setId(17);
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

        main.setMainGames(listOf(game));
        supporting.setSupportGames(listOf(game));

        league.setSeasons(listOf(season));
        season.setGames(listOf(game));
    }

    public static void saveLinks(
            StadiumRepository stadiums,
            CoachRepository coaches,
            TeamManagerRepository managers,
            TeamOwnerRepository owners,
            RefereeRepository referees,
            LeagueRepository leagues,
            SeasonRepository seasons) {

        stadiums.saveAll(listOf(homeStadium, awayStadium));
        coaches.saveAll(listOf(homeCoach, awayCoach));
        managers.saveAll(listOf(homeManager, awayManager));
        owners.saveAll(listOf(homeOwner, awayOwner));
        referees.saveAll(listOf(main, supporting));
        leagues.save(league);
        seasons.save(season);

    }

    public static void saveTeams(
            PlayerRepository players,
            TeamOwnerRepository owners,
            CoachRepository coaches,
            StadiumRepository stadiums,
            TeamManagerRepository managers,
            TeamRepository teams) {

        baseInit();

        players.saveAll(listOf(homePlayer, awayPlayer));
        owners.saveAll(listOf(homeOwner, awayOwner));
        coaches.saveAll(listOf(homeCoach, awayCoach));
        stadiums.saveAll(listOf(homeStadium, awayStadium));
        managers.saveAll(listOf(homeManager, awayManager));
        teams.saveAll(listOf(home, away));
    }

    public static void saveGame(PlayerRepository players,
                                TeamOwnerRepository owners,
                                CoachRepository coaches,
                                StadiumRepository stadiums,
                                TeamManagerRepository managers,
                                TeamRepository teams,
                                RefereeRepository referees,
                                GameRepository games) {

        saveTeams(players, owners, coaches, stadiums, managers, teams);

        referees.saveAll(listOf(main, supporting));
        games.save(game);
    }

    public static void saveAll(PlayerRepository players,
                               TeamOwnerRepository owners,
                               CoachRepository coaches,
                               StadiumRepository stadiums,
                               TeamManagerRepository managers,
                               TeamRepository teams,
                               RefereeRepository referees,
                               GameRepository games,
                               LeagueRepository leagues,
                               SeasonRepository seasons) {


        saveGame(players, owners, coaches, stadiums, managers, teams, referees, games);

        leagues.save(league);
        seasons.save(season);

        createALlLinks();
        saveLinks(stadiums, coaches, managers, owners, referees, leagues, seasons);
    }
}
