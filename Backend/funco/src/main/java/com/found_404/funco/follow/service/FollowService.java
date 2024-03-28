package com.found_404.funco.follow.service;

import static com.found_404.funco.follow.exception.FollowErrorCode.*;
import static com.found_404.funco.member.exception.MemberErrorCode.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.found_404.funco.follow.domain.Follow;
import com.found_404.funco.follow.domain.FollowingCoin;
import com.found_404.funco.follow.domain.repository.FollowRepository;
import com.found_404.funco.follow.domain.repository.FollowingCoinRepository;
import com.found_404.funco.follow.dto.request.FollowingRequest;
import com.found_404.funco.follow.exception.FollowException;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.member.exception.MemberException;
import com.found_404.funco.trade.cryptoPrice.CryptoPrice;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import com.found_404.funco.trade.domain.type.TradeType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class FollowService {

	private final FollowRepository followRepository;
	private final MemberRepository memberRepository;
	private final HoldingCoinRepository holdingCoinRepository;
	private final FollowingCoinRepository followingCoinRepository;
	private final TradeRepository tradeRepository;

	private final CryptoPrice cryptoPrice;

	private static final double FOLLOW_FEE = 0.03;
	private static final int PAGE_SIZE = 2;

	@Transactional
	public void createFollow(FollowingRequest request, Long memberId) {

		// 부모 팔로우 멤버
		Member followingMember = findMemberById(request.followingId());
		// 자식 팔로우 멤버
		Member followerMember = findMemberById(memberId);

		// 팔로우 되어있다면 예외
		Optional<Follow> selectFollow = followRepository.findByFollowingAndFollower(followingMember, followerMember);
		if (selectFollow.isPresent()) {
			throw new FollowException(FOLLOW_DUPLICATED_ERROR);
		}

		// 초기 투자금
		Long investment = request.investment();

		// 팔로워 초기 투자금 차감
		followerMember.decreaseCash(investment);

		// 부모 팔로워 가용 현금
		Long followingCash = followingMember.getCash();

		// 부모의 보유 코인들
		List<HoldingCoin> holdingCoins = holdingCoinRepository.findHoldingCoinByMember(followingMember);

		// 부모의 보유 코인 별 현재 시세 가격
		Map<String, Long> followingCryptoPriceMap = cryptoPrice.getTickerPriceMap(holdingCoins.stream()
			.map(HoldingCoin::getTicker)
			.collect(Collectors.toList()));

		// 부모의 코인 별 보유한 코인 별 가격
		Map<String, Long> followingCryptoToAssetMap = holdingCoins.stream()
			.collect(
				Collectors.toMap(
					HoldingCoin::getTicker,
					holdingCoin -> (long)(followingCryptoPriceMap.get(holdingCoin.getTicker())
						* holdingCoin.getVolume())));
		// 총 보유 자산
		Long asset = followingCryptoToAssetMap.values().stream()
			.mapToLong(Long::longValue)
			.sum() + followingCash;

		// 보유 자산의 비율
		Map<String, Double> assetRatioMap = followingCryptoToAssetMap.entrySet().stream()
			.collect(Collectors.toMap(Map.Entry::getKey,
				entry -> (double)Math.round((double)entry.getValue() / asset * 100) / 100));

		// 가용 현금
		Long followerCash = (long)(investment * ((double)Math.round((double)followingCash / asset * 100) / 100));

		// 팔로우 생성
		Follow follow = Follow.builder()
			.following(followingMember)
			.follower(followerMember)
			.investment(investment)
			.cash(followerCash)
			.settled(Boolean.FALSE)
			.build();

		// 팔로잉 코인, 거래 내역 생성
		Map<FollowingCoin, Trade> followingCoinTradeMap = assetRatioMap.entrySet().stream()
			.collect(Collectors.toMap(entry -> FollowingCoin.builder()
					.follow(follow)
					.ticker(entry.getKey())
					.volume((investment * entry.getValue()) / followingCryptoPriceMap.get(entry.getKey()))
					.averagePrice(followingCryptoPriceMap.get(entry.getKey()))
					.build(),
				entry -> Trade.builder()
					.member(followerMember)
					.ticker(entry.getKey())
					.tradeType(TradeType.BUY)
					.volume((investment * entry.getValue()) / followingCryptoPriceMap.get(entry.getKey()))
					.orderCash((long)(investment * entry.getValue()))
					.price(followingCryptoPriceMap.get(entry.getKey()))
					.status(Boolean.TRUE)
					.build()));

		// 엔티티 insert
		followRepository.save(follow);
		followingCoinRepository.saveAll(followingCoinTradeMap.keySet());
		tradeRepository.saveAll(followingCoinTradeMap.values());
	}

	@Transactional
	public void deleteFollow(Long followId) {
		// 팔로우
		Follow follow = followRepository.findById(followId).orElseThrow(() -> new FollowException(FOLLOW_NOT_FOUND));

		// 팔로잉 멤버
		Member followingMember = findMemberById(follow.getFollowing().getId());

		// 팔로워 멤버
		Member followerMember = findMemberById(follow.getFollower().getId());

		// 팔로잉 코인
		List<FollowingCoin> followingCoins = followingCoinRepository.findFollowingCoinsByFollow(follow);

		// 팔로잉 코인들의 현재 시세
		Map<String, Long> followingCryptoPriceMap = cryptoPrice.getTickerPriceMap(followingCoins.stream()
			.map(FollowingCoin::getTicker)
			.collect(Collectors.toList()));

		// 거래 내역
		List<Trade> trades = followingCoins.stream()
			.map(followingCoin -> Trade.builder()
				.member(followerMember)
				.ticker(followingCoin.getTicker())
				.tradeType(TradeType.SELL)
				.volume(followingCoin.getVolume())
				.orderCash((long)(followingCoin.getVolume() * followingCryptoPriceMap.get(followingCoin.getTicker())))
				.price(followingCryptoPriceMap.get(followingCoin.getTicker()))
				.status(Boolean.TRUE)
				.build())
			.toList();

		// 팔로잉 코인들 가격
		long followAsset = followingCoins.stream()
			.mapToLong(followingCoin -> {
				long asset = (long)(followingCoin.getVolume() * followingCryptoPriceMap.get(followingCoin.getTicker()));
				followingCoin.sellFollowingCoin();
				return asset;
			}).sum();

		// 수익금
		long proceed = followAsset + follow.getCash();

		// 수익률
		double returnRate = (double)(proceed - follow.getInvestment()) / follow.getInvestment();
		// Math.round(((double)(proceed - follow.getInvestment()) / follow.getInvestment()) * 100.0) / 100.0;

		// 수수료
		long commission = proceed - follow.getInvestment() > 0 ? (long)(followAsset * FOLLOW_FEE) : 0;

		// 정산 금액
		long settlement = proceed - commission;

		log.info("수익금 : {}", proceed);
		log.info("수익룰 : {}", returnRate);
		log.info("수수료 : {}", commission);
		log.info("정산 금액 : {}", settlement);

		// follow update
		follow.settleFollow(commission, returnRate, LocalDateTime.now(), settlement);

		// follower member update
		followerMember.increaseCash(settlement);

		// following member update
		if (commission > 0) {
			followingMember.increaseCash(commission);
		}

		// 데이터 insert
		tradeRepository.saveAll(trades);
	}

	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
	}

}
