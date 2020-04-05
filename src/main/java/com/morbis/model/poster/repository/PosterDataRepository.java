package com.morbis.model.poster.repository;

import com.morbis.model.poster.entity.PosterData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PosterDataRepository extends JpaRepository<PosterData,Integer> {
}
