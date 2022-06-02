package com.msamaker.demo.controller;

import com.msamaker.demo.domain.Account;
import com.msamaker.demo.domain.AccountTransaction;
import com.msamaker.demo.domain.Member;
import com.msamaker.demo.domain.dto.TransactionDTO;
import com.msamaker.demo.service.AccountService;
import com.msamaker.demo.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AccountService accountService;

    // 테스트용 멤버 생성
    @PostMapping
    public ResponseEntity<Member> createMember() {
        Member demoMember = Member.builder()
                .id("demo_kim")
                .name("김철수")
                .account(Account.builder().build())
                .build();

        Member member = memberService.createMember(demoMember);
        return ResponseEntity.ok(member);
    }

    // 멤버 기본 정보 출력
    @GetMapping
    public ResponseEntity<Member> getMember() {
        Member member = memberService.getMember("demo_kim");
        return ResponseEntity.ok(member);
    }

    // 잔고 금액 차감 -> 충전 포함 필요
    @PostMapping("charge/{accountId}")
    public ResponseEntity<Account> charge(@PathVariable Long accountId, @RequestBody TransactionDTO dto) {

        log.info("input transaction :\n{}", dto);
        Account account = accountService.charge(accountId,
                AccountTransaction.builder()
                        .amount(dto.getType().updateAmount(dto.getAmount()))
                        .notes(dto.getNotes())
                        .build());
        return ResponseEntity.ok(account);
    }

    @PostMapping("charge/{memberId}/event")
    public ResponseEntity<Account> chargeAuto(@PathVariable String memberId) {
        Account account = accountService.chargeAuto(memberId);
        return ResponseEntity.ok(account);
    }

    // 잔고 히스토리 조회
    @GetMapping("account/{memberId}")
    public ResponseEntity<List<AccountTransaction>> getTransactionListById(@PathVariable String memberId) {
        List<AccountTransaction> transactionList = accountService.getTransactionList(memberId);
        return ResponseEntity.ok(transactionList);
    }

}
