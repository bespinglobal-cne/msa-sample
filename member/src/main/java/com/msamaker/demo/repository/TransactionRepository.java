package com.msamaker.demo.repository;

import com.msamaker.demo.domain.Account;
import com.msamaker.demo.domain.AccountTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<AccountTransaction, Long> {

    List<AccountTransaction> findAllByAccount(Account account);
    List<AccountTransaction> findAllByAccountOrderByDateTimeDesc(Account account);
}
