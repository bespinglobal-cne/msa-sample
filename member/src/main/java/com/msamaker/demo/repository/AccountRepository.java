package com.msamaker.demo.repository;

import com.msamaker.demo.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByMemberId(String memberId);
}
