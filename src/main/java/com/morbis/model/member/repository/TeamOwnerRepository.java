package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.TeamOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamOwnerRepository extends JpaRepository<TeamOwner,Integer> {

    List<TeamOwner> findAllByNameContaining(String query);

    List<Member> findAllByEmailIn(List<String> emails);

}
