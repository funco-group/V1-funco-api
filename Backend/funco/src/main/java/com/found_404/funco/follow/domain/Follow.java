package com.found_404.funco.follow.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.Comment;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.member.domain.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "following_id", nullable = false)
	private Member following;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "follower_id", nullable = false)
	private Member follower;

	@OneToMany(mappedBy = "follow", cascade = CascadeType.ALL)
	private List<FollowingCoin> followingCoins = new ArrayList<>();

	@Comment("초기투자금")
	@Column(nullable = false)
	private Long investment;

	@Comment("가용 현금")
	@Column(nullable = false)
	private Long cash;

	@Comment("수수료")
	private Long commission;

	@Comment("수익률")
	private Double returnRate;

	@Comment("정산날짜")
	private LocalDateTime settleDate;

	@Comment("정산여부")
	private Boolean settled;

	@Comment("정산금액")
	private Long settlement;

	@Builder
	public Follow(Member following, Member follower, Long investment, Long cash, Long commission, Double returnRate,
		LocalDateTime settleDate, Boolean settled, Long settlement) {
		this.following = following;
		this.follower = follower;
		this.investment = investment;
		this.cash = cash;
		this.commission = commission;
		this.returnRate = returnRate;
		this.settleDate = settleDate;
		this.settled = settled;
		this.settlement = settlement;
	}

	public void settleFollow(Long commission, Double returnRate, LocalDateTime settleDate, Long settlement) {
		this.commission = commission;
		this.returnRate = returnRate;
		this.settleDate = settleDate;
		this.settled = true;
		this.settlement = settlement;
	}
}
