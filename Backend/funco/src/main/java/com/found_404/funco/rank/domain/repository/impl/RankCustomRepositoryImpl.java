package com.found_404.funco.rank.domain.repository.impl;

import static com.found_404.funco.follow.domain.QFollow.*;
import static com.found_404.funco.member.domain.QMember.*;
import static com.found_404.funco.trade.domain.QHoldingCoin.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.found_404.funco.follow.dto.FollowingCoinInfo;
import com.found_404.funco.follow.dto.QFollowingCoinInfo;
import com.found_404.funco.member.dto.QMemberInfo;
import com.found_404.funco.rank.domain.repository.RankCustomRepository;
import com.found_404.funco.trade.dto.HoldingCoinInfo;
import com.found_404.funco.trade.dto.QHoldingCoinInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class RankCustomRepositoryImpl implements RankCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<HoldingCoinInfo> findHoldingCoinInfo() {
		return jpaQueryFactory.select(
				new QHoldingCoinInfo(holdingCoin.member.id, holdingCoin.ticker, holdingCoin.volume.sum()
				)
			)
			.from(holdingCoin)
			.where(holdingCoin.volume.ne(0.0))
			.groupBy(holdingCoin.member.id, holdingCoin.ticker)
			.fetch();
	}

	@Override
	public List<FollowingCoinInfo> findFollowingCoin() {
		return jpaQueryFactory.select(
				new QFollowingCoinInfo(new QMemberInfo(member.id, member.nickname,
					member.profileUrl), member.cash, follow.investment.sum().coalesce(0L))
			)
			.from(follow)
			.rightJoin(member).on(follow.following.id.eq(member.id))
			.where(follow.settled.isFalse().or(follow.settled.isNull()))
			.groupBy(member.id)
			.orderBy(follow.investment.sum().desc())
			.fetch();
	}

	@Override
	public List<String> findHoldingCoin() {
		return jpaQueryFactory.select(
				holdingCoin.ticker
			)
			.from(holdingCoin)
			.where(holdingCoin.volume.ne(0.0))
			.distinct()
			.fetch();
	}
}
