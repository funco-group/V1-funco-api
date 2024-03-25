package com.found_404.funco.trade.service;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.member.exception.MemberException;
import com.found_404.funco.trade.cryptoPrice.CryptoPrice;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import com.found_404.funco.trade.domain.type.TradeType;
import com.found_404.funco.trade.dto.Ticker;
import com.found_404.funco.trade.dto.response.HoldingCoinsResponse;
import com.found_404.funco.trade.dto.response.MarketTradeResponse;
import com.found_404.funco.trade.exception.TradeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import static com.found_404.funco.member.exception.MemberErrorCode.NOT_FOUND_MEMBER;
import static com.found_404.funco.trade.exception.TradeErrorCode.INSUFFICIENT_COINS;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final MemberRepository memberRepository;
    private final HoldingCoinRepository holdingCoinRepository;

    private static final Double TRADE_FEE = 0.05;
    private final CryptoPrice cryptoPrice;


    private long getPriceByTicker(String ticker) {
        return cryptoPrice.getTickerPrice(ticker);
    }

    @Transactional
    public MarketTradeResponse marketBuying(long memberId, String ticker, long orderCash) {
        // 회원 조회
        Member member = getMember(memberId);

        // 시세 가져오기
        long currentPrice = getPriceByTicker(ticker);

        // orderCash <= 자산 check, member 원화 감소
        member.decreaseCash(orderCash);

        // 수수료 제외한 구매 개수
        double volume = (double) orderCash / currentPrice;
        volume -= volume * TRADE_FEE;

        // 코인 있으면 더하기 없으면 추가
        Optional<HoldingCoin> holdingCoin = holdingCoinRepository.findByMemberAndTicker(member, ticker);
        if (holdingCoin.isPresent()) {
            holdingCoin.get().increaseVolume(volume, currentPrice);
        } else {
            holdingCoinRepository.save(HoldingCoin.builder()
                    .ticker(ticker)
                    .member(member)
                    .volume(volume)
                    .averagePrice(currentPrice)
                    .build());
        }

        // 거래 데이터 저장
        Trade trade = Trade.builder()
                .ticker(ticker)
                .tradeType(TradeType.BUY)
                .concluded(true)
                .member(member)
                .orderCash(orderCash)
                .price(currentPrice)
                .status(false)
                .volume(volume)
                .build();

        tradeRepository.save(trade);

        // 팔로우 구매 처리 필요 *

        return MarketTradeResponse.builder()
                .ticker(trade.getTicker())
                .price(trade.getPrice())
                .volume(trade.getVolume())
                .build();
    }

    @Transactional
    public MarketTradeResponse marketSelling(long memberId, String ticker, double volume) {
        // 회원 조회
        Member member = getMember(memberId);

        // 시세 가져오기
        long currentPrice = getPriceByTicker(ticker);

        // 수수료 제외한 잔액 증가
        long orderCash = (long) (currentPrice * volume);
        member.increaseCash(orderCash - (long) (orderCash * TRADE_FEE));

        // 코인 감소
        HoldingCoin holdingCoin = holdingCoinRepository.findByMemberAndTicker(member, ticker)
                .orElseThrow(() -> new TradeException(INSUFFICIENT_COINS));
        holdingCoin.decreaseVolume(volume);

        // 데이터 저장
        Trade trade = Trade.builder()
                .ticker(ticker)
                .tradeType(TradeType.SELL)
                .concluded(true)
                .member(member)
                .orderCash(orderCash)
                .price(currentPrice)
                .status(false)
                .volume(volume)
                .build();

        tradeRepository.save(trade);

        // 팔로우 매도 로직 필요

        return MarketTradeResponse.builder()
                .ticker(trade.getTicker())
                .price(trade.getPrice())
                .volume(trade.getVolume())
                .build();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new MemberException(NOT_FOUND_MEMBER));
    }

    public HoldingCoinsResponse getHoldingCoins(Long memberId) {
        Member member = getMember(memberId);

        return HoldingCoinsResponse.builder()
                .holdingCoins(holdingCoinRepository
                        .findByMember(member)
                        .stream()
                        .map(Ticker::getTicker)
                        .collect(Collectors.toList()))
                .build();
    }

}
