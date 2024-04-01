package com.found_404.funco.trade.domain;

import com.found_404.funco.global.util.CommissionUtil;
import com.found_404.funco.global.util.DecimalCalculator;
import com.found_404.funco.global.util.ScaleType;
import com.found_404.funco.trade.exception.TradeException;
import lombok.*;
import org.hibernate.annotations.Comment;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.member.domain.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import static com.found_404.funco.trade.exception.TradeErrorCode.INSUFFICIENT_COINS;

@Entity
@Getter
@ToString
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
		this.averagePrice = (long) (((this.volume * this.averagePrice) + (volume * price)) / (volume + this.volume));
		this.volume = DecimalCalculator.plus(this.volume, volume, ScaleType.VOLUME_SCALE);
	}

	public void decreaseVolume(double volume) {
		if (this.volume < volume) {
			throw new TradeException(INSUFFICIENT_COINS);
		}
		this.volume = DecimalCalculator.minus(this.volume, volume, ScaleType.VOLUME_SCALE);
	}

	public void recoverVolume(double volume) {
		this.volume = DecimalCalculator.plus(this.volume, volume, ScaleType.VOLUME_SCALE);
	}

}
