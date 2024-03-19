package com.found_404.funco.statistics.domain;

import java.time.LocalDate;

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
public class MonthlyStatistics extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Comment("날짜")
	@Column(nullable = false)
	private LocalDate date;

	@Comment("손익")
	@Column(nullable = false)
	private Long returnResult;

	@Comment("수익률")
	@Column(nullable = false)
	private Double returnRate;

	@Comment("누적손익")
	@Column(nullable = false)
	private Long accReturnResult;

	@Comment("누적수익률")
	@Column(nullable = false)
	private Double accReturnRate;

	@Comment("기초자산")
	@Column(nullable = false)
	private Long beginningAsset;

	@Comment("기말자산")
	@Column(nullable = false)
	private Long endingAsset;

	@Builder
	public MonthlyStatistics(Member member, LocalDate date, Long returnResult, Double returnRate, Long accReturnResult,
		Double accReturnRate, Long beginningAsset, Long endingAsset) {
		this.member = member;
		this.date = date;
		this.returnResult = returnResult;
		this.returnRate = returnRate;
		this.accReturnResult = accReturnResult;
		this.accReturnRate = accReturnRate;
		this.beginningAsset = beginningAsset;
		this.endingAsset = endingAsset;
	}
}
