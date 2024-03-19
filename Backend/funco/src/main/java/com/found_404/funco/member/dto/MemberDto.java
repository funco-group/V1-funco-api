package com.found_404.funco.member.dto;

import com.found_404.funco.member.domain.type.MemberStatus;
import lombok.Builder;

@Builder
public record MemberDto(
    Long id,
    String oauthId,
    String nickname,
    String profileUrl,
    String introduction,
    Long cash,
    MemberStatus status

) {


}
