package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Fan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FanRepository extends JpaRepository<Fan,Integer> {
}
