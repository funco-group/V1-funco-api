package com.found_404.funco.trade.domain;

import com.found_404.funco.global.util.CommissionUtil;
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

import static com.found_404.funco.global.util.DecimalCalculator.*;
import static com.found_404.funco.global.util.ScaleType.*;
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
		recoverVolume(CommissionUtil.getVolumeWithoutCommission(volume), price);
	}

	public void decreaseVolume(double volume) {
		if (this.volume < volume) {
			throw new TradeException(INSUFFICIENT_COINS);
		}
		this.volume = minus(this.volume, volume, VOLUME_SCALE);
	}

	public void recoverVolume(double volume, Long price) {
		this.averagePrice = (long) divide((multiple(this.volume, this.averagePrice, NORMAL_SCALE) + multiple(volume, price, NORMAL_SCALE)), plus(volume, this.volume, NORMAL_SCALE), CASH_SCALE);
		this.volume = plus(this.volume, volume, VOLUME_SCALE);
	}

}
