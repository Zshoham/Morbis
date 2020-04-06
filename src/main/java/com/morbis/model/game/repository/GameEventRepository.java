package com.morbis.model.game.repository;

import com.morbis.model.game.entity.GameEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameEventRepository extends JpaRepository<GameEvent,Integer> {
}
