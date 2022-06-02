package com.msamaker.demo.service;


import com.msamaker.demo.domain.MemberPoint;
import com.msamaker.demo.domain.PointTransaction;

import java.util.List;

public interface PointService {

    MemberPoint createPoint(MemberPoint demoPoint);

    MemberPoint getPoint(String memberId);

    MemberPoint charge(String memberId, PointTransaction transaction);

    MemberPoint chargeAuto(String memberId);

    List<PointTransaction> getTransactionList(String memberId);
}
