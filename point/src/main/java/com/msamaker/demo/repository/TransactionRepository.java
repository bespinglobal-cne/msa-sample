package com.msamaker.demo.repository;

import com.msamaker.demo.domain.MemberPoint;
import com.msamaker.demo.domain.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<PointTransaction, Long> {
//List<AccountTransaction> findAllByAccount(Account account);
    List<PointTransaction> findAllByMemberPoint(MemberPoint memberPoint);
    List<PointTransaction> findAllByMemberPointOrderByDateTimeDesc(MemberPoint memberPoint);
}
