package com.msamaker.demo.repository;

import com.msamaker.demo.domain.MemberPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<MemberPoint, Long> {
    MemberPoint findByMemberId(String memberId);

}
