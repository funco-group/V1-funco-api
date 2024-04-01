package com.found_404.funco.follow.domain.repository.impl;

import static com.found_404.funco.follow.domain.QFollow.*;
import static com.found_404.funco.follow.domain.QFollowingCoin.*;
import static com.found_404.funco.follow.exception.FollowErrorCode.*;
import static com.found_404.funco.trade.domain.QHoldingCoin.*;
import static com.querydsl.core.group.GroupBy.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.found_404.funco.follow.domain.repository.QueryDslFollowRepository;
import com.found_404.funco.follow.domain.type.SettleType;
import com.found_404.funco.follow.dto.CoinInfo;
import com.found_404.funco.follow.dto.FollowingInfo;
import com.found_404.funco.follow.dto.HoldingCoinsDto;
import com.found_404.funco.follow.dto.QueryFollowingInfoResult;
import com.found_404.funco.follow.dto.SliceFollowingInfo;
import com.found_404.funco.follow.dto.response.FollowerListResponse;
import com.found_404.funco.follow.dto.response.QFollowerListResponse_FollowerInfo;
import com.found_404.funco.follow.exception.FollowException;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslFollowRepositoryImpl implements QueryDslFollowRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public SliceFollowingInfo findFollowingInfoListByMemberId(Long memberId, Long lastFollowId, int pageSize) {
		List<QueryFollowingInfoResult> queryFollowingInfoResult = jpaQueryFactory
			.select(Projections.constructor(QueryFollowingInfoResult.class, follow.id, follow.following.nickname,
				follow.investment, follow.createdAt, follow.cash))
			.from(follow)
			.where(follow.follower.id.eq(memberId), follow.settled.eq(false), ltFollowId(lastFollowId))
			.orderBy(follow.id.desc())
			.limit(pageSize + 1)
			.fetch();

		List<Long> followIdList = queryFollowingInfoResult.stream().map(
			QueryFollowingInfoResult::followId
		).collect(Collectors.toList());

		boolean last = checkLastPage(followIdList, pageSize);

		Map<Long, List<CoinInfo>> queryFollowingCoinInfoResult = jpaQueryFactory
			.from(followingCoin)
			.where(followingCoin.follow.id.in(followIdList))
			.transform(groupBy(follow.id).as(list(Projections.constructor(CoinInfo.class,
				followingCoin.ticker,
				followingCoin.volume))));

		List<FollowingInfo> followingInfoList = queryFollowingInfoResult.stream()
			.map(result -> FollowingInfo.builder()
				.followId(result.followId())
				.nickname(result.nickname())
				.investment(result.investment())
				.followedAt(result.followedAt())
				.cash(result.cash())
				.coins(queryFollowingCoinInfoResult.getOrDefault(result.followId(), Collections.EMPTY_LIST))
				.build())
			.toList();

		return SliceFollowingInfo.builder()
			.followingInfoList(followingInfoList)
			.last(last)
			.build();
	}

	@Override
	public List<HoldingCoinsDto> findHoldingCoin(Long memberId) {
		return jpaQueryFactory
			.select(Projections.constructor(HoldingCoinsDto.class, holdingCoin.ticker, holdingCoin.ticker))
			.from(holdingCoin)
			.where(holdingCoin.member.id.eq(memberId))
			.fetch();
	}

	@Override
	public FollowerListResponse findFollowerListByMemberIdAndSettleType(Long memberId,
		String settleType,
		Long lastFollowId,
		int pageSize) {
		List<FollowerListResponse.FollowerInfo> followerInfoList = jpaQueryFactory.select(
				new QFollowerListResponse_FollowerInfo(follow.id, follow.createdAt, follow.follower.nickname,
					follow.investment, follow.settlement,
					follow.returnRate, follow.commission, follow.settleDate))
			.from(follow)
			.where(follow.following.id.eq(memberId), checkSettleType(settleType), ltFollowId(lastFollowId))
			.orderBy(follow.id.desc())
			.limit(pageSize + 1)
			.fetch();

		boolean last = checkLastPage(followerInfoList, pageSize);

		return FollowerListResponse.builder()
			.last(last)
			.followers(followerInfoList)
			.build();
	}

	private BooleanExpression ltFollowId(Long followId) {
		if (followId == null) { // 요청이 처음일 때 where 절에 null을 주면 page size만큼 반환
			return null;
		}
		return follow.id.lt(followId);
	}

	private boolean checkLastPage(List<?> results, int pageSize) {
		boolean last = true;
		// 조회한 결과 개수가 요청한 페이지 사이즈보다 크면 뒤에 더 있음, next = true
		if (results.size() > pageSize) {
			last = false;
			results.remove(pageSize);
		}
		return last;
	}

	private BooleanExpression checkSettleType(String settleType) {
		if (settleType == null) {
			throw new FollowException(SETTLE_TYPE_NOT_FOUND);
		} else if (settleType.equals(SettleType.ALL.getValue())) {
			return null;
		} else if (settleType.equals(SettleType.FOLLOWING.getValue())) {
			return follow.settled.eq(false);
		} else if (settleType.equals(SettleType.SETTLED.getValue())) {
			return follow.settled.eq(true);
		} else {
			throw new FollowException(SETTLE_TYPE_NOT_FOUND);
		}
	}
}
