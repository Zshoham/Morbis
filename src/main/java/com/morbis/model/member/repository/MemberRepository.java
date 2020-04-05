package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Integer> {
    List<Member> findByUsername(String username);

    @Query("Select Member from Member where username=:user")
    List<Member> getWhatIWant(@Param("user") String username);
}
