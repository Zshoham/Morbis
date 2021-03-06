package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.repository.GameEventRepository;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.member.repository.RefereeRepository;
import com.morbis.service.viewable.MatchReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RefereeService {

    private final RefereeRepository refereeRepository;
    private final GameRepository gameRepository;
    private final GameEventRepository gameEventRepository;
    private final MemberRepository memberRepository;
    private final Logger logger;


    private static final long UPDATE_PERIOD = 5;

    public RefereeService(RefereeRepository refereeRepository, GameRepository gameRepository, GameEventRepository gameEventRepository, MemberRepository memberRepository) {
        this.refereeRepository = refereeRepository;
        this.gameRepository = gameRepository;
        this.gameEventRepository = gameEventRepository;
        this.memberRepository = memberRepository;
        this.logger = LoggerFactory.getLogger(RefereeService.class);

    }

    //get the games of a referee by ID
    public List<Game> getRefGames(int refID) {
        logger.trace("called function: RefereeService->getRefGames. with the ID of" + refID);
        Referee referee = refereeRepository.findById(refID)
                .orElseThrow(() -> new IllegalArgumentException("referee with id: " + refID + ", does not exist"));

        List<Game> games = new ArrayList<>(referee.getMainGames());
        //check if there are games where the referee was a support referee
        if (referee.getSupportGames() != null) {
            //check if there are games where the referee was a main referee
            games.addAll(referee.getSupportGames());
        }
        logger.info("the games of the referee: " + refID + " has returned. number of games: " + games.size());
        return games;
    }

    public Optional<Game> getOngoingGame(int refID) {
        List<Game> refGames = getRefGames(refID).stream()
                .filter(game ->
                        game.getStartDate().isBefore(LocalDateTime.now()) &&
                                game.getEndDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (refGames.size() > 1)
            throw new IllegalStateException("the referee - refID=" + refID + ", seems to be in more than a single game at the moment");

        return refGames.stream().findAny();
    }

    public List<GameEvent> getGameEvents(int gameID) {
        logger.trace("called function: RefereeService->getGameEvents. with the ID of" + gameID);
        Game resGame = gameRepository.findById(gameID)
                .orElseThrow(() -> new IllegalArgumentException("game with id: " + gameID + ", does not exist"));
        List<GameEvent> res = resGame.getEvents();
        //check if there are any events
        if (res == null || res.isEmpty()) {
            logger.info("getGameEvents returned null");
            return new ArrayList<>();
        }
        logger.info("getOnGoingGameEvents returned list of events. game ID: " + gameID + " number of events: " + res.size());
        return res;
    }

    public boolean updateGameEvent(int refID, GameEvent updated, int gameID) {
        logger.trace("called function: RefereeService->updateGameEvent. referee: " + refID);

        Game game = gameRepository.findById(gameID).orElseThrow(() ->
                new IllegalArgumentException("trying to add event to nonexistent game"));
        updated.setGame(game);

        boolean isInUpdatePeriod = game.getEndDate().plusHours(UPDATE_PERIOD).isAfter(LocalDateTime.now())
                && game.getEndDate().isBefore(LocalDateTime.now());

        //check if the update period hasn't passed
        if (isInUpdatePeriod) {
            //check if its the main referee
            if (game.getMainRef().getId() == refID) {
                gameEventRepository.save(updated);
                logger.info("updateGameEvent returned true");
                return true;
            }
        }
        logger.info("updateGameEvent returned false");
        return false;
    }

    public List<GameEvent> getOnGoingGameEvents(int refID) {
        logger.trace("called function: RefereeService->getOnGoingGameEvents. ref ID: " + refID);
        List<Game> refGames = getRefGames(refID).stream()
                .filter(game ->
                        game.getStartDate().isBefore(LocalDateTime.now()) &&
                        game.getEndDate().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());

        if (refGames.size() > 1)
            throw new IllegalStateException("the referee - refID=" + refID + ", seems to be in more than a single game at the moment");

        return refGames.stream().
                map(Game::getEvents).
                findFirst().
                orElse(new ArrayList<>());
    }

    public boolean updateOnGoingGameEvent(int refID, GameEvent updated, int gameID) {
        logger.trace("called function: RefereeService->updateOnGoingGameEvent. referee ID: " + refID);

        Game game = gameRepository.findById(gameID).orElseThrow(() ->
                new IllegalArgumentException("trying to add event to nonexistent game"));


        if (game.getEndDate().isBefore(updated.getDate()))
            throw new IllegalStateException("referee " + refID +" is trying to add event after the game is over");

        int gameTime = (int) ChronoUnit.MINUTES.between(game.getStartDate(), updated.getDate());
        updated.setGameTime(gameTime);

        //check if the game is on-going
        if (game.getEndDate().isAfter(LocalDateTime.now()) && game.getStartDate().isBefore(LocalDateTime.now())) {
            boolean isGameRef = game.getMainRef().getId() == refID
                    || game.getSupportingRefs()
                        .stream()
                        .map(Referee::getId)
                        .collect(Collectors.toList())
                        .contains(refID);

            if (isGameRef) {
                updated.setGame(game);
                game.getEvents().add(updated);
                game.getFollowers().forEach(member -> member.getEventBackLog().add(updated));
                gameEventRepository.save(updated);
                gameRepository.save(game);
                memberRepository.saveAll(game.getFollowers());

                logger.info("updateOnGoingGameEvent returned true. referee ID: " + refID);
                return true;
            }
        }
        logger.info("updateOnGoingGameEvent failed. referee ID: " + refID + ", gameID: " + gameID);
        return false;
    }

    public MatchReport getMatchReport(int refID, int gameID) {
        Referee ref = refereeRepository.findById(refID)
                .orElseThrow(() -> new IllegalArgumentException("referee with id " + refID + " does not exist"));

        Game game = gameRepository.findById(gameID)
                .orElseThrow(() -> new IllegalArgumentException("game with id " + gameID + " does not exist"));

        if (!game.getMainRef().equals(ref))
            throw new IllegalStateException("referee with id=" + refID + " is not the main referee of the game=" + gameID);

        return new MatchReport(game);
    }
}
