package com.found_404.funco.trade.service;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.trade.cryptoPrice.RealtimeCryptoPrice;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import com.found_404.funco.trade.domain.type.TradeType;
import com.found_404.funco.trade.dto.response.MarketTradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TradeService {
    private final TradeRepository tradeRepository;
    private final RealtimeCryptoPrice realtimeCryptoPrice;
    private final MemberRepository memberRepository;
    private final HoldingCoinRepository holdingCoinRepository;

    private long getPriceByTicker(String ticker) {
        return realtimeCryptoPrice.getTickerPrice(ticker);
    }

    @Transactional
    public MarketTradeResponse marketBuying(Long memberId, String ticker, Long orderCash) {
        Member member = getMember(memberId);

        // 시세 가져오기
        long currentPrice = getPriceByTicker(ticker);

        // orderCash <= 자산 check
        double volume = (double) orderCash / currentPrice;

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

        // 데이터 저장
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

        // 팔로우 구매

        return MarketTradeResponse.builder()
                .ticker(trade.getTicker())
                .price(trade.getPrice())
                .volume(trade.getVolume())
                .build();
    }

    private Member getMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("not found member"));
    }

    @Transactional
    public MarketTradeResponse marketSelling(Long memberId, String ticker, Double volume) {
        Member member = getMember(memberId);
        // 시세 가져오기
        long currentPrice = getPriceByTicker(ticker);
        // orderCash <= 자산 check
        long orderCash = (long) (currentPrice * volume);

        // 코인 감소
        HoldingCoin holdingCoin = holdingCoinRepository.findByMemberAndTicker(member, ticker)
                .orElseThrow(() -> new RuntimeException("수량 부족"));

        holdingCoin.decreaseVolume(volume, currentPrice);

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

        // 팔로우 매도

        return MarketTradeResponse.builder()
                .ticker(trade.getTicker())
                .price(trade.getPrice())
                .volume(trade.getVolume())
                .build();
    }

}
