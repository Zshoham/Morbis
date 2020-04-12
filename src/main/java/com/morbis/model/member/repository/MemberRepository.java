package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    Optional<Member> findDistinctByUsername(String name);
}
