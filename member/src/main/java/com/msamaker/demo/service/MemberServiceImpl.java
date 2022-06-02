package com.msamaker.demo.service;

import com.msamaker.demo.domain.Member;
import com.msamaker.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member createMember(Member member) {
        // auto-create
        Optional<Member> existed = memberRepository.findById(member.getId());
        return existed.orElseGet(() -> memberRepository.save(member));
    }

    @Override
    public Member getMember(String id) {
        Member member = memberRepository.findById(id).orElseThrow();
        return member;
    }
}
