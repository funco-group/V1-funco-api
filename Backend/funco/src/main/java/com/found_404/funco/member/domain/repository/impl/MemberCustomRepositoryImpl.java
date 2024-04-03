package com.found_404.funco.member.domain.repository.impl;

import static com.found_404.funco.follow.domain.QFollow.*;
import static com.found_404.funco.member.domain.QMember.*;
import static com.found_404.funco.trade.domain.QHoldingCoin.*;
import static com.found_404.funco.trade.domain.QTrade.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.found_404.funco.member.domain.repository.MemberCustomRepository;
import com.found_404.funco.member.dto.MemberInfo;
import com.found_404.funco.member.dto.QMemberInfo;
import com.found_404.funco.trade.dto.HoldingCoinsDto;
import com.found_404.funco.trade.dto.QHoldingCoinsDto;
import com.found_404.funco.trade.dto.QRecentTradedCoin;
import com.found_404.funco.trade.dto.RecentTradedCoin;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HoldingCoinsDto> findHoldingCoinsByMemberId(Long memberId) {
		return jpaQueryFactory
			.select(new QHoldingCoinsDto(holdingCoin.ticker, holdingCoin.volume))
			.from(holdingCoin)
			.where(holdingCoin.member.id.eq(memberId))
			.fetch();
	}

	@Override
	public MemberInfo findMemberInfoByMemberId(Long memberId) {
		return jpaQueryFactory
			.select(new QMemberInfo(member.nickname, member.profileUrl, member.introduction, member.cash))
			.from(member)
			.where(member.id.eq(memberId))
			.fetchFirst();
	}

	@Override
	public Long getFollowingCashByMemberId(Long memberId) {
		return jpaQueryFactory.select(follow.investment.sum().coalesce(0L))
			.from(follow)
			.where(follow.follower.id.eq(memberId),
				follow.settled.isNull().or(follow.settled.isFalse()))
			.fetchFirst();
	}

	@Override
	public Long getFollowerCashByMemberId(Long memberId) {
		return jpaQueryFactory.select(follow.investment.sum().coalesce(0L))
			.from(follow)
			.where(follow.following.id.eq(memberId),
				follow.settled.isNull().or(follow.settled.isFalse()))
			.fetchFirst();
	}

	@Override
	public Boolean isFollowedByMemberId(Long loginId, Long memberId) {
		return !jpaQueryFactory
			.from(follow)
			.where(follow.follower.id.eq(loginId),
				follow.following.id.eq(memberId),
				follow.settled.isNull().or(follow.settled.isFalse()))
			.fetch().isEmpty();
	}

	@Override
	public List<RecentTradedCoin> findRecentTradedCoinByMemberId(Long memberId) {
		return jpaQueryFactory
			.select(new QRecentTradedCoin(trade.ticker, trade.createdAt))
			.from(trade)
			.where(trade.member.id.eq(memberId))
			.groupBy(trade.ticker)
			.orderBy(trade.createdAt.desc())
			.limit(3)
			.fetch();
	}
}
