package com.morbis;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.Season;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.CoachRepository;
import com.morbis.model.member.repository.RefereeRepository;
import com.morbis.model.member.repository.TeamManagerRepository;
import com.morbis.model.member.repository.TeamOwnerRepository;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.service.auth.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class MorbisApplication implements ApplicationRunner {

    public static boolean DEV_MODE = false;

    private final Logger logger = LoggerFactory.getLogger(MorbisApplication.class);

    private final AuthService authService;

    private final StadiumRepository stadiums;
    private final TeamRepository teams;
    private final GameRepository games;
    private final LeagueRepository leagues;
    private final SeasonRepository seasons;
    private final CoachRepository coaches;
    private final TeamManagerRepository managers;
    private final TeamOwnerRepository owners;
    private final RefereeRepository referees;


    public MorbisApplication(@Lazy AuthService authService,
                             StadiumRepository stadiums,
                             TeamRepository teams,
                             GameRepository games,
                             LeagueRepository leagues,
                             SeasonRepository seasons,
                             CoachRepository coaches,
                             TeamManagerRepository managers,
                             TeamOwnerRepository owners,
                             RefereeRepository referees) {

        this.authService = authService;
        this.stadiums = stadiums;
        this.teams = teams;
        this.games = games;
        this.leagues = leagues;
        this.seasons = seasons;
        this.coaches = coaches;
        this.managers = managers;
        this.owners = owners;
        this.referees = referees;
    }

    public static void main(String[] args) {
        SpringApplication.run(MorbisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.getOptionNames().contains("dev")) {
            DEV_MODE = true;
            populateDevData();
        }

        if (!args.getOptionNames().contains("setup"))
            return;

        logger.info("Creating new admin from command line arguments");

        String admin_username = args.getOptionValues("Ausername").get(0);
        String admin_password = args.getOptionValues("Apassword").get(0);
        String admin_name = args.getOptionValues("Aname").get(0);
        String admin_email = args.getOptionValues("Aemail").get(0);

        String rep_username = args.getOptionValues("Rusername").get(0);
        String rep_password = args.getOptionValues("Rpassword").get(0);
        String rep_name = args.getOptionValues("Rname").get(0);
        String rep_email = args.getOptionValues("Remail").get(0);

        Admin admin = Admin.newAdmin()
                .fromMember(admin_username, admin_password, admin_name, admin_email)
                .build();

        AssociationRep rep = AssociationRep.newAssociationRep()
                .fromMember(rep_username, rep_password, rep_name, rep_email)
                .build();

        authService.register(admin);
        authService.register(rep);


    }

    @SafeVarargs
    public static <T> List<T> listOf(T... items) {
        return Stream.of(items).collect(Collectors.toList());
    }

    private void populateDevData() {
        Stadium homeStadium;
        Stadium awayStadium;
        Player homePlayer;
        Player awayPlayer;
        TeamOwner homeOwner;
        TeamOwner awayOwner;
        TeamManager homeManager;
        TeamManager awayManager;
        Coach homeCoach;
        Coach awayCoach;
        Team home;
        Team away;
        Referee main;
        Referee supporting;
        Game game;
        Season season;
        League league;

        homeStadium = Stadium.newStadium("home stadium")
                .build();

        awayStadium = Stadium.newStadium("away stadium")
                .build();

        homePlayer = Player.newPlayer(LocalDate.now(), "ST")
                .fromMember("home player", "Password123", "name", "email@morbis.xyz")
                .build();

        awayPlayer = Player.newPlayer(LocalDate.now(), "ST")
                .fromMember("away player", "Password123", "name", "email@morbis.xyz")
                .build();

        homeOwner = TeamOwner.newTeamOwner()
                .fromMember("home owner", "Password123", "home name", "email@morbis.xyz")
                .build();

        awayOwner = TeamOwner.newTeamOwner()
                .fromMember("away owner", "Password123", "away name", "email@morbis.xyz")
                .build();

        homeManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("home manager", "Password123", "name", "email@morbis.xyz")
                .build();

        awayManager = TeamManager.newTeamManager(ManagerPermissions.all)
                .fromMember("away manager", "Password123", "name", "email@morbis.xyz")
                .build();

        homeCoach = Coach.newCoach("school", "attack")
                .fromMember("home coach", "Password123", "home name", "email@morbis.xyz")
                .build();

        awayCoach = Coach.newCoach("school", "attack")
                .fromMember("away coach", "Password123", "away name", "email@morbis.xyz")
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
                .fromMember("main", "Password123", "main name", "email")
                .build();

        supporting = Referee.newReferee("school")
                .fromMember("supporting", "Password123", "supporting name", "email")
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
        game.setFollowers(listOf(homePlayer, awayPlayer, main, supporting));


        // save teams
        authService.register(homePlayer);
        authService.register(awayPlayer);
        authService.register(homeOwner);
        authService.register(awayOwner);
        authService.register(homeCoach);
        authService.register(awayCoach);
        authService.register(homeManager);
        authService.register(awayManager);
        
        stadiums.saveAll(listOf(homeStadium, awayStadium));
        teams.saveAll(listOf(home, away));

        // save game
        authService.register(main);
        authService.register(supporting);
        games.save(game);

        // save leagues
        leagues.save(league);
        seasons.save(season);

        // create links
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
        homePlayer.setGamesFollowing(listOf(game));
        awayPlayer.setGamesFollowing(listOf(game));


        main.setMainGames(listOf(game));
        supporting.setSupportGames(listOf(game));
        main.setGamesFollowing(listOf(game));
        supporting.setGamesFollowing(listOf(game));

        league.setSeasons(listOf(season));
        season.setGames(listOf(game));

        // save links
        stadiums.saveAll(listOf(homeStadium, awayStadium));
        coaches.saveAll(listOf(homeCoach, awayCoach));
        managers.saveAll(listOf(homeManager, awayManager));
        owners.saveAll(listOf(homeOwner, awayOwner));
        referees.saveAll(listOf(main, supporting));
        leagues.save(league);
        seasons.save(season);

        AssociationRep rep = AssociationRep.newAssociationRep()
                .fromMember("representative", "Password123", "representative", "rep@morbis.xyz")
                .build();
        rep.setGamesFollowing(listOf(game));
        authService.register(rep);

        Fan bob = Fan.newFan()
                .fromMember("bob", "Password123", "burgers", "bob@morbis.syz")
                .build();
        authService.register(bob);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
