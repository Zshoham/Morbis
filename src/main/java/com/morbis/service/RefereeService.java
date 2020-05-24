package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.repository.GameEventRepository;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.member.repository.RefereeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RefereeService {
    private final RefereeRepository refereeRepository;
    private final GameRepository gameRepository;
    private final GameEventRepository gameEventRepository;
    private final Logger logger;


    private static final long UPDATE_PERIOD = 5;
    private static final long GAME_LENGTH = 90;

    public RefereeService(RefereeRepository refereeRepository, GameRepository gameRepository, GameEventRepository gameEventRepository) {
        this.refereeRepository = refereeRepository;
        this.gameRepository = gameRepository;
        this.gameEventRepository = gameEventRepository;
        this.logger = LoggerFactory.getLogger(RefereeService.class);

    }

    //get the games of a referee by ID
    public List<Game> getRefGames(int refID) {
        logger.trace("called function: RefereeService->getRefGames. with the ID of" + refID);
        Optional<Referee> referee = refereeRepository.findById(refID);
        if (referee.isEmpty())
            return new ArrayList<>();
        List<Game> games = referee.get().getMainGames();
        //check if there are games where the referee was a support referee
        if (referee.get().getSupportGames() != null) {
            //check if there are games where the referee was a main referee
            if (games == null)
                games = referee.get().getSupportGames();
            else
                games.addAll(referee.get().getSupportGames());
        }
        logger.info("the games of the referee: " + refID + " has returned. number of games: " + games.size());
        return games;
    }

    public Game getGame(int gameID) {
        logger.trace("called function: RefereeService->getGame. with the ID of" + gameID);
        Optional<Game> res = gameRepository.findById(gameID);
        //check if there are any games
        if (res.isEmpty()) {
            logger.info("game does not exist");
            return null;
        }
        logger.info("getGame returned a game");
        return res.get();
    }

    public List<GameEvent> getGameEvents(int gameID) {
        logger.trace("called function: RefereeService->getGameEvents. with the ID of" + gameID);
        Game resGame = getGame(gameID);
        List<GameEvent> res = resGame.getEvents();
        //check if there are any events
        if (res == null || res.isEmpty()) {
            logger.info("getGameEvents returned null");
            return null;
        }
        logger.info("getOnGoingGameEvents returned list of events. game ID: " + gameID + " number of events: " + res.size());
        return res;
    }

    public boolean updateGameEvent(int refID, GameEvent updated) {
        logger.trace("called function: RefereeService->updateGameEvent. referee: " + refID);
        Game game = updated.getGame();
        //check if the update period hasn't passed
        if (game.getEndDate().plusMinutes(UPDATE_PERIOD).isAfter(LocalDateTime.now())) {
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

    public List<GameEvent> getOnGoingGameEvents(int gameID) {
        logger.trace("called function: RefereeService->getOnGoingGameEvents. game ID: " + gameID);
        Game resGame = getGame(gameID);
        //check if the game is on-going
        if (resGame.getEndDate().isBefore(LocalDateTime.now())) {
            List<GameEvent> res = resGame.getEvents();
            //check if any events exists
            if (res == null || res.isEmpty()) {
                logger.info("getOnGoingGameEvents returned null. game ID: " + gameID);
                return null;
            }
            logger.info("getOnGoingGameEvents returned list of events. game ID: " + gameID + " number of events: " + res.size());
            return res;
        }
        logger.info("getOnGoingGameEvents returned null. game ID: " + gameID);
        return null;
    }

    public boolean updateOnGoingGameEvent(int refID, GameEvent updated) {
        logger.trace("called function: RefereeService->updateOnGoingGameEvent. referee ID: " + refID);
        Game game = updated.getGame();
        //check if the game is on-going
        if (game.getEndDate().isAfter(LocalDateTime.now()) && game.getStartDate().isBefore(LocalDateTime.now())) {
            //check if the update period hasn't passed
            if (game.getStartDate().plusMinutes(GAME_LENGTH).isAfter(LocalDateTime.now())) {

                boolean isGameRef = game.getMainRef().getId() == refID
                        || game.getSupportingRefs()
                            .stream()
                            .map(Referee::getId)
                            .collect(Collectors.toList())
                            .contains(refID);

                if (isGameRef) {
                    gameEventRepository.save(updated);
                    logger.info("updateOnGoingGameEvent returned true. referee ID: " + refID);
                    return true;
                }
            }
        }
        logger.info("updateOnGoingGameEvent returned false. referee ID: " + refID);
        return false;
    }

}
