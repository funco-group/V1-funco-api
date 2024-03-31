package com.found_404.funco.member.domain;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.global.util.CommissionUtil;
import com.found_404.funco.member.domain.type.MemberStatus;
import com.found_404.funco.trade.exception.TradeErrorCode;
import com.found_404.funco.trade.exception.TradeException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Comment("OAuth 코드")
    @Column(nullable = false)
    private String oauthId;

    @Comment("닉네임")
    @Column(length = 20)
    private String nickname;

    @Comment("프로필 이미지 URL")
    @Column(length = 2100)
    private String profileUrl;

    @Comment("한 줄 소개")
    @Column(length = 100)
    private String introduction;

    @Comment("가용 현금")
    @Column(nullable = false)
    private Long cash;

    @Comment("회원 유형")
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private MemberStatus status;

    private static final Double COMMISSION = 0.05;

    @Builder
    public Member(String oauthId, String nickname, String profileUrl, String introduction,
        Long cash,
        MemberStatus status) {
        this.oauthId = oauthId;
        this.nickname = nickname;
        this.profileUrl = profileUrl;
        this.introduction = introduction;
        this.cash = cash;
        this.status = status;
    }

    public void decreaseCash(long orderCash) {
        if (this.cash < orderCash) {
            throw new TradeException(TradeErrorCode.INSUFFICIENT_COINS);
        }
        this.cash -= orderCash;
    }

    public void increaseCash(long orderCash) {
        this.cash += CommissionUtil.getCashWithoutCommission(orderCash);
    }

    public void recoverCash(long cash) {
        this.cash += cash;
    }
}
