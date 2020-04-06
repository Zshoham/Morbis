package com.morbis.service;

import com.morbis.model.game.repository.GameEventRepository;
import com.morbis.model.game.repository.GameRepository;

public class RefereeService {
    private GameRepository gameRepository;
    private GameEventRepository gameEventRepository;

    public RefereeService(GameRepository gameRepository, GameEventRepository gameEventRepository) {
        this.gameRepository = gameRepository;
        this.gameEventRepository = gameEventRepository;
    }
}
