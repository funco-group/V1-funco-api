package com.found_404.funco.rank.dto.response;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import com.found_404.funco.member.dto.MemberInfo;

import lombok.Builder;

@Builder
public record RankResponse(
	Long rank,
	MemberInfo member,
	Double returnRate,
	Long totalAsset,
	Long followingAsset
) {
	public static List<RankResponse> from(Set<ZSetOperations.TypedTuple<Object>> typedTuples, Long offset) {
		if (CollectionUtils.isEmpty(typedTuples)) {
			return Collections.emptyList();
		}
		AtomicInteger index = new AtomicInteger(0); // 인덱스를 저장할 AtomicInteger 생성
		return typedTuples.stream()
			.map(tuple -> {
				RankResponse rankResponse = (RankResponse)tuple.getValue();
				return RankResponse.builder()
					.rank(offset + Long.valueOf(index.incrementAndGet())) // 인덱스를 증가시키고 rank에 저장
					.member(rankResponse.member())
					.returnRate(rankResponse.returnRate())
					.totalAsset(rankResponse.totalAsset())
					.followingAsset(rankResponse.followingAsset())
					.build();
			})
			.collect(Collectors.toList());
	}
}
