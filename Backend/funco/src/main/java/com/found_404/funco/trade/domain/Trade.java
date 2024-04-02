package com.found_404.funco.trade.domain;

import org.hibernate.annotations.Comment;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.domain.type.TradeType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Trade extends BaseEntity {

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

	@Comment("체결 가격")
	private Long price;

	@Builder
	public Trade(Member member, String ticker, TradeType tradeType, Double volume, Long orderCash, Long price) {

		this.member = member;
		this.ticker = ticker;
		this.tradeType = tradeType;
		this.volume = volume;
		this.orderCash = orderCash;
		this.price = price;
	}
}
