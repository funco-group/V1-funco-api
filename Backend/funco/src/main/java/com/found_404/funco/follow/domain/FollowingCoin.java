package com.found_404.funco.follow.domain;

import com.found_404.funco.global.util.CommissionUtil;
import com.found_404.funco.global.util.DecimalCalculator;
import com.found_404.funco.global.util.ScaleType;
import com.found_404.funco.trade.exception.TradeException;
import org.hibernate.annotations.Comment;

import com.found_404.funco.global.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.found_404.funco.global.util.DecimalCalculator.*;
import static com.found_404.funco.global.util.ScaleType.NORMAL_SCALE;
import static com.found_404.funco.global.util.ScaleType.VOLUME_SCALE;
import static com.found_404.funco.trade.exception.TradeErrorCode.INSUFFICIENT_COINS;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FollowingCoin extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follow_id", nullable = false)
	private Follow follow;

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
	public FollowingCoin(Follow follow, String ticker, Double volume, Long averagePrice) {
		this.follow = follow;
		this.ticker = ticker;
		this.volume = volume;
		this.averagePrice = averagePrice;
	}

	public void sellFollowingCoin() {
		this.volume = 0.0;
	}

	public void increaseVolume(double volume, Long price) {
		this.averagePrice = (long) divide((multiple(this.volume, this.averagePrice, NORMAL_SCALE) + multiple(volume, price, NORMAL_SCALE))
				, plus(volume, this.volume, VOLUME_SCALE), NORMAL_SCALE);
		this.volume += CommissionUtil.getVolumeWithoutCommission(volume);
	}

	public void decreaseVolume(double volume) {
		if (this.volume < volume) {
			throw new TradeException(INSUFFICIENT_COINS);
		}
		this.volume = minus(this.volume, volume, VOLUME_SCALE);
	}
}
