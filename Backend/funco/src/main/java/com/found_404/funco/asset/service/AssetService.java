package com.found_404.funco.asset.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.found_404.funco.asset.dto.response.HistoryResponse;
import com.found_404.funco.asset.dto.type.AssetType;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import org.springframework.stereotype.Service;

import com.found_404.funco.asset.dto.HoldingCoinInfo;
import com.found_404.funco.asset.dto.response.CashResponse;
import com.found_404.funco.asset.dto.response.CryptoResponse;
import com.found_404.funco.asset.dto.response.TotalAssetResponse;
import com.found_404.funco.follow.domain.Follow;
import com.found_404.funco.follow.domain.repository.FollowRepository;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetService {

	private final FollowRepository followRepository;
	private final HoldingCoinRepository holdingCoinRepository;
	private final TradeRepository tradeRepository;


	public CashResponse getMemberCash(Member member) {
		return new CashResponse(member.getCash());
	}

	public TotalAssetResponse getMemberTotalAsset(Member member) {

		// 가용 현금
		Long memberCash = member.getCash();

		// 해당 멤버가 팔로우 중인 총 초기 투자 금액
		List<Follow> follows = followRepository.findAllByFollowerAndSettledFalse(member);
		Long memberFollwingInvestment = follows.stream()
			.mapToLong(Follow::getInvestment)
			.sum();

		// 해당 멤버가 보유 중인 코인
		// HoldingCoinInfo에 담아서 응답으로 컨트롤러에 넘겨줌
		List<HoldingCoin> holdingCoins = holdingCoinRepository.findHoldingCoinByMember(member);
		List<HoldingCoinInfo> memberHoldingCoinInfos = holdingCoins.stream()
			.map(holdingCoin -> new HoldingCoinInfo(
				holdingCoin.getTicker(),
				holdingCoin.getVolume(),
				holdingCoin.getAveragePrice()
			))
			.toList();

		return TotalAssetResponse.builder()
			.cash(memberCash)
			.followingInvestment(memberFollwingInvestment)
			.holdingCoinInfos(memberHoldingCoinInfos)
			.build();
	}

	public CryptoResponse getCrypto(Member member, String ticker) {
		Optional<HoldingCoin> optionalHoldingCoin = holdingCoinRepository.findByMemberAndTicker(member, ticker);
		return new CryptoResponse(optionalHoldingCoin.isPresent() ? optionalHoldingCoin.get().getVolume() : 0);
	}

	public List<HistoryResponse> getMemberHistory(Member member) {

		// HistoryResponse들을 담을 리스트
		List<HistoryResponse> historyResponses = new ArrayList<>();

		// 직접 투자
		List<Trade> trades = tradeRepository.findAllByMemberAndStatusFalse(member);
		trades.forEach(trade -> {
			HistoryResponse response = HistoryResponse.builder()
					.date(trade.getCreatedAt())
					.name(trade.getTicker())
					.assetType(AssetType.COIN)
					.tradeType(trade.getTradeType().toString())
					.volume(trade.getVolume())
					.orderCash(trade.getOrderCash())
					.price(trade.getPrice())
					.build();
			historyResponses.add(response);
		});

		// 팔로우 투자
		// 해당 멤버가(팔로워가) 정산한 팔로우 거래 내역
		List<Follow> followings = followRepository.findAllByFollowerAndSettledTrue(member);

		followings.forEach(following -> {
			HistoryResponse response = HistoryResponse.builder()
					.date(following.getSettleDate())
					.name(following.getFollower().getNickname())
					.assetType(AssetType.FOLLOW)
					.tradeType("FOLLOWING")
					.volume((double) 1)
					.orderCash(following.getInvestment())
					.settlement(following.getSettlement())
					.build();
			historyResponses.add(response);
		});


		// 해당 멤버를 팔로우 한 사람(팔로잉)이 정산한 거래 내역
		List<Follow> followers = followRepository.findAllByFollowingAndSettledTrue(member);
		followers.forEach(follower -> {
			HistoryResponse response = HistoryResponse.builder()
					.date(follower.getSettleDate())
					.name(follower.getFollower().getNickname())
					.assetType(AssetType.FOLLOW)
					.tradeType("FOLLOWER")
					.volume((double) 1)
					.orderCash(follower.getInvestment())
					.commission(follower.getCommission())
					.build();
			historyResponses.add(response);
		});

		// DTO가 담긴 리스트를 date 순으로 정렬
		return historyResponses.stream()
				.sorted(Comparator.comparing(HistoryResponse::date).reversed()).collect(Collectors.toList());
	}


}
