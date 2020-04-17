package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.repository.GameEventRepository;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.member.repository.RefereeRepository;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RefereeService {
    private RefereeRepository refereeRepository;
    private GameRepository gameRepository;
    private GameEventRepository gameEventRepository;

    private long updatePeriod = 5;

    public RefereeService(RefereeRepository refereeRepository, GameRepository gameRepository, GameEventRepository gameEventRepository) {
        this.refereeRepository = refereeRepository;
        this.gameRepository = gameRepository;
        this.gameEventRepository = gameEventRepository;
    }
    //get the games of a referee by ID
    public List<Game> getRefGames(int refID){

        Optional<Referee> referee = refereeRepository.findById(refID);
        if(referee.isEmpty())
            return new ArrayList<Game>();
        List<Game> games = referee.get().getMainGames();
        //check if there are games where the referee was a support referee
        if(referee.get().getSupportGames()!=null) {
            //check if there are games where the referee was a main referee
            if(games==null)
                games = referee.get().getSupportGames();
            else
                games.addAll(referee.get().getSupportGames());
        }
        return games;
    }

    public Game getGame(int gameID){
        Optional<Game> res = gameRepository.findById(gameID);
        //check if there are any game
        if(res==null || res.isEmpty())
            return null;
        return res.get();
    }
    public List<GameEvent> getGameEvents(int gameID){
        Game resGame = getGame(gameID);
        List<GameEvent> res = resGame.getEvents();
        //check if there are any events
        if(res==null || res.isEmpty())
            return null;
        return res;
    }
    public boolean updateGameEvent(int refID,GameEvent updated) {
       Game game = updated.getGame();
        //check if the update period hasn't passed
        if(game.getEndDate().plusHours(updatePeriod).isAfter(LocalDateTime.now())) {
            //check if its the main referee
            if (game.getMainRef().getId() == refID) {
               gameEventRepository.save(updated);
               return true;
           }
       }
       return false;
    }
    public List<GameEvent> getOnGoingGameEvents(int gameID){
        Game resGame = getGame(gameID);
        //check if the game is on-going
        if(resGame.getEndDate().isBefore(LocalDateTime.now())) {
            List<GameEvent> res = resGame.getEvents();
            //check if any events exists
            if (res == null || res.isEmpty())
                return null;
            return res;
        }
        return null;
    }
    public boolean updateOnGoingGameEvent(int refID,GameEvent updated) {
        Game game = updated.getGame();
        //check if the game is on-going
        if(game.getEndDate().isBefore(LocalDateTime.now())) {
            //check if the update period hasn't passed
            if (game.getEndDate().plusHours(updatePeriod).isAfter(LocalDateTime.now())) {
                //check if its the main referee
                if (game.getMainRef().getId() == refID) {
                    gameEventRepository.save(updated);
                    return true;
                }
            }
        }
        return false;
    }

}
