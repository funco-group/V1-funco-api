package com.found_404.funco.trade.domain;

import org.hibernate.annotations.Comment;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class HoldingCoin extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Comment("코인명")
	@Column(length = 20, nullable = false)
	private String ticker;

	@Comment("개수")
	@Column(nullable = false)
	private Double volume;

	@Comment("평균단가")
	@Column(nullable = false)
	private Long averagePrice;

	@Builder
	public HoldingCoin(Member member, String ticker, Double volume, Long averagePrice) {
		this.member = member;
		this.ticker = ticker;
		this.volume = volume;
		this.averagePrice = averagePrice;
	}

	public void increaseVolume(double volume, Long price) {
		this.volume += volume;
		// 평단가 재조정
	}

	public void decreaseVolume(double volume, Long price) {
		if (this.volume < volume) {
			throw new RuntimeException("수량 부족");
		}
		this.volume -= volume;
		// 평단가 재조정
	}

}
