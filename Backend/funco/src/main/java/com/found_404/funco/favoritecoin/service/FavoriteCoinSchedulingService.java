package com.found_404.funco.favoritecoin.service;

import static com.found_404.funco.global.type.RedisZSetType.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.found_404.funco.favoritecoin.domain.FavoriteCoin;
import com.found_404.funco.favoritecoin.domain.repository.FavoriteCoinRepository;
import com.found_404.funco.favoritecoin.dto.FavoriteCoinInfo;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.member.exception.MemberErrorCode;
import com.found_404.funco.member.exception.MemberException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteCoinSchedulingService {
	private final RedisTemplate<String, Object> favoriteCoinRedisTemplate;
	private final RedisTemplate<String, Object> favoriteCoinZSetRedisTemplate;
	private final FavoriteCoinRepository favoriteCoinRepository;
	private final MemberRepository memberRepository;

	@Scheduled(cron = "0 0 7 * * *", zone = "Asia/Seoul")
	public void runSchedulingProcess() {
		// Redis에서 스케줄링 돌린 이후에 관심 코인을 업데이트 한 memberId 가져오기
		Set<String> memberIdSet = getUpdatedMemberIdsFromRedis();

		for (String memberId : memberIdSet) {
			// Redis에서 사용자의 관심 코인 정보 가져오기
			FavoriteCoinInfo favoriteCoinInfo = getFavoriteCoinInfoFromRedis(memberId);

			if (favoriteCoinInfo != null) {
				// 사용자 정보 가져오기
				Member member = getMemberFromDatabase(memberId);

				// Redis에 있는 ticker 정보 가져오기
				Set<String> tickers = favoriteCoinInfo.getTickerSet();

				// Redis에 없는데 MariaDB에 있는 ticker 삭제
				deleteMissingTickersFromDatabase(member, tickers);

				// MariaDB에 없는 ticker 삽입
				insertMissingTickersToDatabase(member, tickers);
			}
		}

		// Redis에서 빈 Set 행 삭제
		deleteEmptySet(memberIdSet);

		// 스케줄링 작업 후 ZSet에서 모든 항목을 정리
		refreshZSetAfterScheduling();
	}

	// Redis에서 스케줄링 돌린 이후에 관심 코인을 업데이트 한 memberId 가져오기
	private Set<String> getUpdatedMemberIdsFromRedis() {
		return Objects.requireNonNull(favoriteCoinZSetRedisTemplate.opsForZSet().rangeByScore(
				FAVORITE_COIN_ZSET.toString(),
				LocalDateTime.now().minusDays(1).toEpochSecond(ZoneOffset.UTC),
				Double.POSITIVE_INFINITY
			)).stream()
			.map(Object::toString)
			.collect(Collectors.toSet());
	}

	// Redis에서 사용자의 관심 코인 정보 가져오기
	private FavoriteCoinInfo getFavoriteCoinInfoFromRedis(String memberId) {
		return (FavoriteCoinInfo)favoriteCoinRedisTemplate.opsForValue().get(memberId);
	}

	// 사용자 정보 가져오기
	private Member getMemberFromDatabase(String memberId) {
		return memberRepository.findById(Long.valueOf(memberId)).orElseThrow(() -> new MemberException(
			MemberErrorCode.NOT_FOUND_MEMBER));
	}

	// Redis에 없는데 MariaDB에 있는 ticker 삭제
	private void deleteMissingTickersFromDatabase(Member member, Set<String> tickers) {
		favoriteCoinRepository.deleteAllByMemberAndTickerNotIn(member, tickers);
	}

	// MariaDB에 없는 ticker 삽입
	private void insertMissingTickersToDatabase(Member member, Set<String> tickers) {
		List<String> favoriteCoinTickers = favoriteCoinRepository.findFavoriteCoinTickersByMemberId(member.getId());

		favoriteCoinRepository.saveAll(tickers.stream()
			.filter(ticker -> !favoriteCoinTickers.contains(ticker))
			.map(ticker -> FavoriteCoin.builder()
				.member(member)
				.ticker(ticker)
				.build())
			.collect(Collectors.toList()));
	}

	// redis 에서 empty set row 삭제
	private void deleteEmptySet(Set<String> updatedKeys) {
		updatedKeys.stream().forEach(key -> {
			// Fetch the FavoriteCoinInfo object from Redis
			FavoriteCoinInfo favoriteCoinInfo = (FavoriteCoinInfo)favoriteCoinRedisTemplate.opsForValue().get(key);

			if (favoriteCoinInfo == null || favoriteCoinInfo.getTickerSet().isEmpty()) { // delete the key from Redis
				favoriteCoinRedisTemplate.delete(key);
			}
		});
	}

	// 스케줄링 작업 후 ZSet에서 모든 항목을 정리
	public void refreshZSetAfterScheduling() {
		favoriteCoinRedisTemplate.delete(FAVORITE_COIN_ZSET.toString());// ZSet 전체를 삭제
	}
}
