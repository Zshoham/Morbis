package com.morbis.service;

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
import com.morbis.service.viewable.ViewableProperties;
import com.morbis.service.viewable.SearchResult;
import com.morbis.service.viewable.ViewableEntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

import static com.morbis.service.viewable.ViewableEntityType.*;

@Service
@Transactional
public class GuestService {

    private final MemberRepository memberRepository;
    private final GameRepository gameRepository;
    private final LeagueRepository leagueRepository;
    private final SeasonRepository seasonRepository;
    private final CoachRepository coachRepository;
    private final PlayerRepository playerRepository;
    private final RefereeRepository refereeRepository;
    private final TeamOwnerRepository teamOwnerRepository;
    private final TeamManagerRepository teamManagerRepository;
    private final StadiumRepository stadiumRepository;
    private final TeamRepository teamRepository;
    private final Logger logger;

    @Autowired
    public GuestService(MemberRepository memberRepository,
                        GameRepository gameRepository,
                        LeagueRepository leagueRepository,
                        SeasonRepository seasonRepository,
                        CoachRepository coachRepository,
                        PlayerRepository playerRepository,
                        RefereeRepository refereeRepository,
                        TeamOwnerRepository teamOwnerRepository,
                        TeamManagerRepository teamManagerRepository,
                        StadiumRepository stadiumRepository,
                        TeamRepository teamRepository) {

        this.memberRepository = memberRepository;
        this.gameRepository = gameRepository;
        this.leagueRepository = leagueRepository;
        this.seasonRepository = seasonRepository;
        this.coachRepository = coachRepository;
        this.playerRepository = playerRepository;
        this.refereeRepository = refereeRepository;
        this.teamOwnerRepository = teamOwnerRepository;
        this.teamManagerRepository = teamManagerRepository;
        this.stadiumRepository = stadiumRepository;
        this.teamRepository = teamRepository;
        this.logger = LoggerFactory.getLogger(GuestService.class);
    }

    public Collection<SearchResult> searchData(List<ViewableEntityType> filter, String query) {
        logger.trace("called function: GuestService->searchData.");
        Collection<SearchResult> results = new ConcurrentLinkedDeque<>();

        ViewableEntityType.match(filter)
                .inCase(GAME,            () -> results.addAll(findGames(query)))
                .inCase(LEAGUE,          () -> results.addAll(findLeagues(query)))
                .inCase(SEASON,          () -> results.addAll(findSeasons(query)))
                .inCase(COACH,           () -> results.addAll(findCoaches(query)))
                .inCase(PLAYER,          () -> results.addAll(findPlayers(query)))
                .inCase(REFEREE,         () -> results.addAll(findReferees(query)))
                .inCase(TEAM_OWNER,      () -> results.addAll(findTeamOwners(query)))
                .inCase(TEAM_MANAGER,    () -> results.addAll(findTeamManagers(query)))
                .inCase(STADIUM,         () -> results.addAll(findStadiums(query)))
                .inCase(TEAM,            () -> results.addAll(findTeams(query)))
                .execute();

        return results;
    }

    public ViewableProperties<?> getData(SearchResult result) {
        logger.trace("called function: GuestService->getData.");
        var res = new Object() {
            ViewableProperties<?> data;
        };

        ViewableEntityType.match(result.getType())
                .inCase(GAME, () -> res.data = ViewableProperties.from(
                        gameRepository.findById(result.getId()).orElse(new Game())))

                .inCase(LEAGUE, () -> res.data = ViewableProperties.from(
                        leagueRepository.findById(result.getId()).orElse(new League())))

                .inCase(SEASON, () -> res.data = ViewableProperties.from(
                        seasonRepository.findById(result.getId()).orElse(new Season())))

                .inCase(COACH, () -> res.data = ViewableProperties.from(
                        coachRepository.findById(result.getId()).orElse(new Coach())))

                .inCase(PLAYER, () -> res.data = ViewableProperties.from(
                        playerRepository.findById(result.getId()).orElse(new Player())))

                .inCase(REFEREE, () -> res.data = ViewableProperties.from(
                        refereeRepository.findById(result.getId()).orElse(new Referee())))

                .inCase(TEAM_OWNER, () -> res.data = ViewableProperties.from(
                        teamOwnerRepository.findById(result.getId()).orElse(new TeamOwner())))

                .inCase(TEAM_MANAGER, () -> res.data = ViewableProperties.from(
                        teamManagerRepository.findById(result.getId()).orElse(new TeamManager())))

                .inCase(STADIUM, () -> res.data = ViewableProperties.from(
                        stadiumRepository.findById(result.getId()).orElse(new Stadium())))

                .inCase(TEAM, () -> res.data = ViewableProperties.from(
                        teamRepository.findById(result.getId()).orElse(new Team())))

                .execute();

        return res.data;
    }

    private Collection<SearchResult> findGames(String query) {
        logger.trace("called function: GuestService->findGames. with the query: " + query);
        return gameRepository.findAllContainingQuery(query).stream()
                .map(game -> new SearchResult(
                        game.getId(),
                        Game.getDescription(game),
                        GAME))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findLeagues(String query) {
        logger.trace("called function: GuestService->findLeagues. with the query: " + query);
        return leagueRepository.findAllByNameContaining(query).stream()
                .map(league -> new SearchResult(league.getId(), league.getName(), LEAGUE))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findSeasons(String query) {
        logger.trace("called function: GuestService->findSeasons. with the query: " + query);
        try {
            Integer.parseInt(query);
        } catch (NumberFormatException e) {
            return Collections.emptyList();
        }

        return seasonRepository.findAllByYear(Integer.parseInt(query)).stream()
                .map(season -> new SearchResult(season.getId(), String.valueOf(season.getYear()), SEASON))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findCoaches(String query) {
        logger.trace("called function: GuestService->findCoaches. with the query: " + query);
        return coachRepository.findAllByNameContaining(query).stream()
                .map(coach -> new SearchResult(coach.getId(), coach.getName(), COACH))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findPlayers(String query) {
        logger.trace("called function: GuestService->findPlayers. with the query: " + query);
        return playerRepository.findAllByNameContaining(query).stream()
                .map(player -> new SearchResult(player.getId(), player.getName(), PLAYER))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findReferees(String query) {
        logger.trace("called function: GuestService->findReferees. with the query: " + query);
        return refereeRepository.findAllByNameContaining(query).stream()
                .map(referee -> new SearchResult(referee.getId(), referee.getName(), REFEREE))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findTeamOwners(String query) {
        logger.trace("called function: GuestService->findTeamOwners. with the query: " + query);
        return teamOwnerRepository.findAllByNameContaining(query).stream()
                .map(teamOwner -> new SearchResult(teamOwner.getId(), teamOwner.getName(), TEAM_OWNER))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findTeamManagers(String query) {
        logger.trace("called function: GuestService->findTeamManagers. with the query: " + query);
        return teamManagerRepository.findAllByNameContaining(query).stream()
                .map(teamManager -> new SearchResult(teamManager.getId(), teamManager.getName(), TEAM_MANAGER))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findStadiums(String query) {
        logger.trace("called function: GuestService->findStadiums. with the query: " + query);
        return stadiumRepository.findAllByNameContaining(query).stream()
                .map(stadium -> new SearchResult(stadium.getId(), stadium.getName(), STADIUM))
                .collect(Collectors.toList());
    }

    private Collection<SearchResult> findTeams(String query) {
        logger.trace("called function: GuestService->findTeams. with the query: " + query);
        return teamRepository.findAllByNameContaining(query).stream()
                .map(team -> new SearchResult(team.getId(), team.getName(), TEAM))
                .collect(Collectors.toList());
    }

}
