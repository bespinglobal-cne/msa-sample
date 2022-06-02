package com.msamaker.demo.service;

import com.msamaker.demo.domain.Member;

public interface MemberService {

    Member createMember(Member member);

    Member getMember(String id);
}
