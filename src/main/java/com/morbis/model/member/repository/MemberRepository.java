package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    Optional<Member> findDistinctByUsername(String name);

    List<Member> findAllByMemberRoleNotIn(List<MemberRole> roles);

    boolean existsMemberByUsername(String username);
}
