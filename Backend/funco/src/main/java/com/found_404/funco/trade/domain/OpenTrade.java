package com.found_404.funco.trade.domain;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.domain.type.TradeType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OpenTrade extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Comment("코인명")
    @Column(length = 20)
    private String ticker;

    @Comment("거래 구분")
    @Enumerated(value = EnumType.STRING)
    private TradeType tradeType;

    @Comment("수량")
    private Double volume;

    @Comment("주문 금액")
    private Long orderCash;

    @Comment("가격")
    private Long price;

    @Comment("팔로우 여부")
    private Boolean status;

    @Builder
    public OpenTrade(Member member, String ticker, TradeType tradeType, Double volume, Long orderCash, Long price,
                 Boolean status) {
        this.member = member;
        this.ticker = ticker;
        this.tradeType = tradeType;
        this.volume = volume;
        this.orderCash = orderCash;
        this.price = price;
        this.status = status;
    }

    public static Trade toTrade(OpenTrade openTrade) {
        return Trade.builder()
                .ticker(openTrade.getTicker())
                .volume(openTrade.getVolume())
                .status(openTrade.getStatus())
                .price(openTrade.getPrice())
                .orderCash(openTrade.getOrderCash())
                .tradeType(openTrade.getTradeType())
                .member(openTrade.getMember())
                .build();
    }

}
