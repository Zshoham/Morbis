package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Coach;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoachRepository extends JpaRepository<Coach,Integer> {
}
