package com.found_404.funco.asset.service;

import java.util.List;
import java.util.Optional;

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
}
