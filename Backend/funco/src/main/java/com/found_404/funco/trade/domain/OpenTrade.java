package com.found_404.funco.trade.domain;

import static com.found_404.funco.global.util.DecimalCalculator.*;
import static com.found_404.funco.global.util.ScaleType.*;

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

    @Comment("구매한 가격")
    private Long buyPrice;

    @Builder
    public OpenTrade(Member member, String ticker, TradeType tradeType, Double volume, Long orderCash, Long price, Long buyPrice) {
        this.member = member;
        this.ticker = ticker;
        this.tradeType = tradeType;
        this.volume = volume;
        this.orderCash = orderCash;
        this.price = price;
        this.buyPrice = buyPrice;
    }

    public static Trade toTrade(OpenTrade openTrade, Long tradePrice) {
        return Trade.builder()
            .ticker(openTrade.getTicker())
            .volume(openTrade.getVolume())
            .price(tradePrice) // 시장가
            .orderCash((long)multiple(openTrade.volume, tradePrice, CASH_SCALE)) // 시장가로 체결
            .tradeType(openTrade.getTradeType())
            .member(openTrade.getMember())
            .build();
    }

}
