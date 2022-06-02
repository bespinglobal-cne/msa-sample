package com.msamaker.demo.controller;

import com.msamaker.demo.domain.MemberPoint;
import com.msamaker.demo.domain.PointTransaction;
import com.msamaker.demo.domain.dto.TransactionDTO;
import com.msamaker.demo.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    // 테스트용 member의 포인트 정보 생성
    @PostMapping
    public ResponseEntity<MemberPoint> createPoint() {
        MemberPoint demoPoint = MemberPoint.builder()
                .memberId("demo_kim")
                .build();

        MemberPoint point = pointService.createPoint(demoPoint);
        return ResponseEntity.ok(point);
    }

    // member 포인트 정보 출력
    @GetMapping
    public ResponseEntity<MemberPoint> getPoint() {
        MemberPoint point = pointService.getPoint("demo_kim");
        return ResponseEntity.ok(point);
    }

    // 포인트 변경 -> 충전, 차감 구분 필요
    @PostMapping("pay/{memberId}")
    public ResponseEntity<MemberPoint> pay(@PathVariable String memberId, @RequestBody TransactionDTO dto) {

        MemberPoint point = pointService.charge(memberId,
                PointTransaction.builder()
                .amount(dto.getAmount())
                .notes(dto.getNotes())
                .build());

        return ResponseEntity.ok(point);
    }

    // 이벤트성 충전 +5000
    @PostMapping("pay/{memberId}/event")
    public ResponseEntity<MemberPoint> payTemp(@PathVariable String memberId) {
        MemberPoint point = pointService.chargeAuto(memberId);
        return ResponseEntity.ok(point);
    }

    // 포인트 사용내역 히스토리 조회
    @GetMapping("{memberId}")
    public ResponseEntity<List<PointTransaction>> getHistory(@PathVariable String memberId) {
        List<PointTransaction> transactionList = pointService.getTransactionList(memberId);
        return ResponseEntity.ok(transactionList);
    }

}
