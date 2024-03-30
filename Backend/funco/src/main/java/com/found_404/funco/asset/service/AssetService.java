package com.found_404.funco.asset.service;

import com.found_404.funco.asset.dto.response.CashResponse;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.member.exception.MemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.found_404.funco.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final MemberRepository memberRepository;

    public CashResponse getMemberCash(long memberId) {
        return new CashResponse(memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER))
                .getCash());
    }


}
