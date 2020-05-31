package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    Optional<Member> findDistinctByUsername(String name);

    @Query("select m from Member m where not exists" +
            "(select r from m.memberRole r where r in :roles)")
    List<Member> findAllByMemberRoleNotIn(List<MemberRole> roles);
}
