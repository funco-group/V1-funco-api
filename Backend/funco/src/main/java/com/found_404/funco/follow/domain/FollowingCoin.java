package com.found_404.funco.follow.domain;

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
}
