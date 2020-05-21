package com.morbis.model.member.repository;

import com.morbis.model.member.entity.MemberSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSearchRepository extends JpaRepository<MemberSearch, Integer> {
}
