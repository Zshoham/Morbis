package com.morbis.model.member.repository;

import com.morbis.model.member.entity.MemberComplaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberComplaintRepository extends JpaRepository<MemberComplaint,Integer> {
}
