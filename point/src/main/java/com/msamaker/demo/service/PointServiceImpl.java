package com.msamaker.demo.service;

import com.msamaker.demo.domain.MemberPoint;
import com.msamaker.demo.domain.PointTransaction;
import com.msamaker.demo.repository.PointRepository;
import com.msamaker.demo.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final TransactionRepository transactionRepository;

    @Override
    public MemberPoint createPoint(MemberPoint demoPoint) {
        return pointRepository.save(demoPoint);
    }

    @Override
    public MemberPoint getPoint(String memberId) {
        MemberPoint point = pointRepository.findByMemberId(memberId);
        return point;
    }

    @Override
    public MemberPoint charge(String memberId, PointTransaction transaction) {
        MemberPoint target = pointRepository.findByMemberId(memberId);

        Long balance = target.getBalance() + transaction.getAmount();

//        if (balance < 0)
//            throw new OutOfBalanceException();

        transaction.setMemberPoint(target);
        transaction.setBalance(balance);

        target.setBalance(balance);
        target.addTransaction(transaction);

        return pointRepository.save(target);
    }

    @Override
    public MemberPoint chargeAuto(String memberId) {
        MemberPoint target = pointRepository.findByMemberId(memberId);

        Long amount = 5000L;
        Long balance = target.getBalance() + amount;

        PointTransaction transaction = PointTransaction.builder()
                .memberPoint(target)
                .amount(amount)
                .balance(balance)
                .notes("이벤트 포인트 적립")
                .build();

        target.setBalance(balance);
        target.addTransaction(transaction);

        return pointRepository.save(target);
    }

    @Override
    public List<PointTransaction> getTransactionList(String memberId) {
        MemberPoint target = pointRepository.findByMemberId(memberId);
        return transactionRepository.findAllByMemberPointOrderByDateTimeDesc(target);
    }


}
