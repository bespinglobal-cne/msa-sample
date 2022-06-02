package com.msamaker.demo.service;

import com.msamaker.demo.domain.Account;
import com.msamaker.demo.domain.AccountTransaction;
import com.msamaker.demo.repository.AccountRepository;
import com.msamaker.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public Account charge(Long accountId, AccountTransaction transaction) {
        Account target = accountRepository.findById(accountId).orElseThrow(() -> new NoSuchElementException("계좌가 존재하지 않습니다."));

        Long balance = target.getBalance() + transaction.getAmount();

        transaction.setAccount(target);
        transaction.setBalance(balance);

        target.setBalance(balance);
        target.addTransaction(transaction);

        return accountRepository.save(target);
    }

    @Override
    public Account charge(String memberId, AccountTransaction transaction) {
        Account target = accountRepository.findByMemberId(memberId);

        Long balance = target.getBalance() + transaction.getAmount();

//        if (balance < 0)
//            throw new OutOfBalanceException();

        transaction.setAccount(target);
        transaction.setBalance(balance);

        target.setBalance(balance);
        target.addTransaction(transaction);

        return accountRepository.save(target);
    }

    @Override
    public Account chargeAuto(Long accountId) {
        Account target = accountRepository.findById(accountId).orElseThrow(() -> new NoSuchElementException("계좌가 존재하지 않습니다."));

        Long amount = 10000L;
        Long balance = target.getBalance() + amount;

        AccountTransaction transaction = AccountTransaction.builder()
                .account(target)
                .amount(amount)
                .balance(balance)
                .notes("이벤트 충전")
                .build();

        target.setBalance(balance);
        target.addTransaction(transaction);

        return accountRepository.save(target);
    }

    @Override
    public Account chargeAuto(String memberId) {
        Account target = accountRepository.findByMemberId(memberId);
        Long amount = 5000L;
        Long balance = target.getBalance() + amount;

        AccountTransaction transaction = AccountTransaction.builder()
                .account(target)
                .amount(amount)
                .balance(balance)
                .notes("이벤트 충전")
                .build();

        target.setBalance(balance);
        target.addTransaction(transaction);

        return accountRepository.save(target);
    }

    @Override
    public List<AccountTransaction> getTransactionList(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow();
        return transactionRepository.findAllByAccount(account);
    }

    @Override
    public List<AccountTransaction> getTransactionList(String memberId) {
        Account account = accountRepository.findByMemberId(memberId);
        return transactionRepository.findAllByAccountOrderByDateTimeDesc(account);
    }
}