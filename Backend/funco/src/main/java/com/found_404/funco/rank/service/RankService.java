package com.found_404.funco.rank.service;

import static com.found_404.funco.global.util.DecimalCalculator.*;
import static com.found_404.funco.global.util.ScaleType.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.member.exception.MemberErrorCode;
import com.found_404.funco.member.exception.MemberException;
import com.found_404.funco.trade.domain.OpenTrade;
import com.found_404.funco.trade.domain.repository.OpenTradeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.found_404.funco.follow.dto.FollowingCoinInfo;
import com.found_404.funco.rank.domain.repository.RankCustomRepository;
import com.found_404.funco.rank.domain.type.RankType;
import com.found_404.funco.rank.dto.response.RankResponse;
import com.found_404.funco.trade.cryptoPrice.CryptoPrice;
import com.found_404.funco.trade.dto.HoldingCoinInfo;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RankService {

	private final RedisTemplate<String, Object> rankZSetRedisTemplate;
	private final RankCustomRepository rankCustomRepository;
	private final CryptoPrice cryptoPrice;
	private final OpenTradeRepository openTradeRepository;
	private final MemberRepository memberRepository;

	private final static long INIT_CASH = 10_000_000;
	private final static long PERCENT = 100;

	public Page<RankResponse> readRanking(String type, Pageable pageable) {
		ZSetOperations<String, Object> zSetOperations = rankZSetRedisTemplate.opsForZSet();
		String zSetName = type.toUpperCase().equals(RankType.ASSET.toString()) ? RankType.ASSET.getDescription() :
			RankType.FOLLOWER_CASH.getDescription();

		// 페이징 처리된 결과 가져오기
		Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSetOperations.reverseRangeWithScores(zSetName,
			pageable.getOffset(),
			pageable.getOffset() + pageable.getPageSize() - 1);

		// 가져온 결과를 RankResponse 객체로 변환
		List<RankResponse> rankResponses = RankResponse.from(typedTuples, pageable.getOffset());
		// 전체 결과 수 가져오기
		Long totalElements = zSetOperations.zCard(zSetName);

		// Page 객체 생성
		return new PageImpl<>(rankResponses, pageable, totalElements);
	}

	// 배치 전 - 스케줄링 돌릴 메서드
	@Scheduled(cron = "0 0/1 * * * *", zone = "Asia/Seoul") // 1분마다 실행
	@PostConstruct
	public void runSchedulingProcess() {
		log.debug("스케줄링 작업 시간 : " + LocalDateTime.now());
		// redis 비우는 작업
		clearRankingZSets();
		// 코인 및 가격 정보 조회
		Map<String, Long> tickerPrice = cryptoPrice.getTickerPriceMap(rankCustomRepository.findHoldingCoin());
		// 코인 자산 및 팔로워 자산 계산 및 랭킹 업데이트
		calculateAndSetRanking(tickerPrice);
	}

	// redis ZSET 비우는 메서드
	private void clearRankingZSets() {
		rankZSetRedisTemplate.opsForZSet().removeRange(RankType.FOLLOWER_CASH.getDescription(), 0, -1);
		rankZSetRedisTemplate.opsForZSet().removeRange(RankType.ASSET.getDescription(), 0, -1);
	}

	// 코인 자산 및 팔로워 자산 계산 및 랭킹 업데이트
	private void calculateAndSetRanking(Map<String, Long> tickerPrice) {
		// 코인 자산 계산
		Map<Long, Long> holdingCoins = calculateHoldingCoins(tickerPrice);

		// 팔로워 자산 계산
		List<FollowingCoinInfo> followingCoinInfos = rankCustomRepository.findFollowingCoinInfo();

		// 랭킹 업데이트
		followingCoinInfos.forEach(info -> {
			Member member = memberRepository.findById(info.memberInfo().id())
					.orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER));
			List<OpenTrade> openTradeList = openTradeRepository.findAllByMember(member);
			long openOrderCash = openTradeList.stream().map(OpenTrade::getOrderCash)
                    .reduce(0L, Long::sum);
			// 미체결 금액

			long totalAsset = info.cash() + (holdingCoins.getOrDefault(info.memberInfo().id(), 0L)) +
					rankCustomRepository.getInvestmentByMemberId(info.memberInfo().id()) + openOrderCash;

			updateRankingInRedis(RankResponse.builder()
				.member(info.memberInfo())
				.returnRate(
					multiple(divide(totalAsset - INIT_CASH, INIT_CASH, NORMAL_SCALE), PERCENT, RETURN_RATE_SCALE))
				.totalAsset(totalAsset)
				.followingAsset(info.followingAsset())
				.build());
		});
	}

	// 코인 자산 계산
	private Map<Long, Long> calculateHoldingCoins(Map<String, Long> tickerPrice) {
		if (CollectionUtils.isEmpty(tickerPrice)) {
			return Collections.emptyMap();
		}

		List<HoldingCoinInfo> holdingCoinInfos = rankCustomRepository.findHoldingCoinInfo();
		return holdingCoinInfos.stream()
			.collect(Collectors.toMap(
				HoldingCoinInfo::memberId,
				info -> Math.round(tickerPrice.get(info.ticker()) * info.volume()),
				Long::sum
			));
	}

	// redis에 랭킹 업데이트
	private void updateRankingInRedis(RankResponse rankResponse) {
		rankZSetRedisTemplate.opsForZSet()
			.add(RankType.FOLLOWER_CASH.getDescription(), rankResponse, rankResponse.followingAsset());
		rankZSetRedisTemplate.opsForZSet()
			.add(RankType.ASSET.getDescription(), rankResponse, rankResponse.totalAsset());
	}
}
