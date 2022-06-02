package com.msamaker.demo.service;

import com.msamaker.demo.domain.Account;
import com.msamaker.demo.domain.AccountTransaction;

import java.util.List;

public interface AccountService {


    Account chargeAuto(Long accountId);

    Account chargeAuto(String memberId);

    List<AccountTransaction> getTransactionList(Long accountId);

    List<AccountTransaction> getTransactionList(String memberId);

    Account charge(Long accountId, AccountTransaction transaction);

    Account charge(String memberId, AccountTransaction transaction);
}
