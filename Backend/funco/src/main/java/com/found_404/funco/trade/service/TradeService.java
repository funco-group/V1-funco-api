package com.found_404.funco.trade.service;

import com.found_404.funco.follow.service.FollowTradeService;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.trade.cryptoPrice.CryptoPrice;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.OpenTrade;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;
import com.found_404.funco.trade.domain.repository.OpenTradeRepository;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import com.found_404.funco.trade.domain.type.TradeType;
import com.found_404.funco.trade.dto.OpenTradeDto;
import com.found_404.funco.trade.dto.Ticker;
import com.found_404.funco.trade.dto.TradeDto;
import com.found_404.funco.trade.dto.response.HoldingCoinsResponse;
import com.found_404.funco.trade.dto.response.MarketTradeResponse;
import com.found_404.funco.trade.exception.TradeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.found_404.funco.global.util.DecimalCalculator.*;
import static com.found_404.funco.global.util.ScaleType.*;
import static com.found_404.funco.trade.exception.TradeErrorCode.*;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;
    private final HoldingCoinRepository holdingCoinRepository;
    private final OpenTradeRepository openTradeRepository;

    private final CryptoPrice cryptoPrice;
    private final FollowTradeService followTradeService;
    private final MemberRepository memberRepository;


    private long getPriceByTicker(String ticker) {
        return cryptoPrice.getTickerPrice(ticker);
    }

    @Transactional
    public MarketTradeResponse marketBuying(Member member, String ticker, long orderCash) {
        // 시세 가져오기
        long currentPrice = getPriceByTicker(ticker);

        // orderCash <= 자산 check, member 원화 감소
        member.decreaseCash(orderCash);
        memberRepository.save(member);

        // 구매 개수
        double volume = divide(orderCash, currentPrice, VOLUME_SCALE);

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
                .member(member)
                .orderCash(orderCash)
                .price(currentPrice)
                .status(false)
                .volume(volume)
                .build();

        tradeRepository.save(trade);

        // 팔로우 연동
        followTradeService.followTrade(trade);

        return MarketTradeResponse.builder()
                .ticker(trade.getTicker())
                .price(trade.getPrice())
                .volume(trade.getVolume())
                .build();
    }

    @Transactional
    public MarketTradeResponse marketSelling(Member member, String ticker, double volume) {
        // 시세 가져오기
        long currentPrice = getPriceByTicker(ticker);

        // 수수료 제외한 잔액 증가
        long orderCash = (long) (multiple(currentPrice, volume, NORMAL_SCALE));
        member.increaseCash(orderCash);
        memberRepository.save(member);

        // 코인 감소
        HoldingCoin holdingCoin = holdingCoinRepository.findByMemberAndTicker(member, ticker)
                .orElseThrow(() -> new TradeException(INSUFFICIENT_COINS));

        decreaseHoldingCoin(holdingCoin, volume);

        // 데이터 저장
        Trade trade = Trade.builder()
                .ticker(ticker)
                .tradeType(TradeType.SELL)
                .member(member)
                .orderCash(orderCash)
                .price(currentPrice)
                .status(false)
                .volume(volume)
                .build();

        tradeRepository.save(trade);

        // 팔로우 연동
        followTradeService.followTrade(trade);

        return MarketTradeResponse.builder()
                .ticker(trade.getTicker())
                .price(trade.getPrice())
                .volume(trade.getVolume())
                .build();
    }

    public HoldingCoinsResponse getHoldingCoins(Member member) {
        return HoldingCoinsResponse.builder()
                .holdingCoins(holdingCoinRepository
                        .findByMember(member)
                        .stream()
                        .map(Ticker::getTicker)
                        .collect(Collectors.toList()))
                .build();
    }

    @Transactional(readOnly = true)
    public List<TradeDto> getOrders(Member member, String ticker, Boolean follow, Pageable pageable) {
        return tradeRepository.findMyTradeHistoryByTicker(member.getId(), follow, ticker, pageable)
                .stream()
                .map(TradeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OpenTradeDto> getOpenOrders(Member member, Boolean follow, String ticker, Pageable pageable) {
        // 멤버 아이디, 코인 유무, id 역순,
        return openTradeRepository.findMyOpenTrade(member.getId(), follow, ticker, pageable)
                .stream()
                .map(OpenTradeDto::fromEntity)
                .collect(Collectors.toList());

    }

    @Transactional
    public void deleteOpenTrade(Member member, Long openTradeId) {

        OpenTrade openTrade = openTradeRepository.findById(openTradeId)
                .orElseThrow(() -> new TradeException(NOT_FOUND_TRADE));
        if (!openTrade.getMember().getId().equals(member.getId())) {
            throw new TradeException(TRADE_UNAUTHORIZED);
        }

        openTradeRepository.delete(openTrade);

        // 돈 또는 코인 회수
        if (openTrade.getTradeType().equals(TradeType.BUY)) {
            member.recoverCash(openTrade.getOrderCash());
            memberRepository.save(member);
        } else {
            HoldingCoin holdingCoin = holdingCoinRepository.findByMemberAndTicker(openTrade.getMember(), openTrade.getTicker())
                    .orElseThrow(() -> new TradeException(INSUFFICIENT_COINS));
            holdingCoin.recoverVolume(openTrade.getVolume());
        }
    }

    @Transactional
    public void limitBuying(Member member, String ticker, Long price, Double volume) {
        // 돈 확인 및 감소
        long orderCash = (long) (price * volume);
        member.decreaseCash(orderCash);
        memberRepository.save(member);

        // 미체결 거래 등록
        OpenTrade openTrade = openTradeRepository.save(OpenTrade.builder()
                .ticker(ticker)
                .tradeType(TradeType.BUY)
                .member(member)
                .orderCash(orderCash)
                .price(price)
                .volume(volume)
                .status(Boolean.FALSE)
                .build());

        cryptoPrice.addTrade(openTrade.getTicker(), openTrade.getId(), openTrade.getTradeType(), openTrade.getPrice());
    }

    @Transactional
    public void limitSelling(Member member, String ticker, Long price, Double volume) {
        // 코인 확인 및 팔려고 등록한 만큼 빼기
        Optional<HoldingCoin> optionalHoldingCoin = holdingCoinRepository.findByMemberAndTicker(member, ticker);
        if (optionalHoldingCoin.isEmpty() || optionalHoldingCoin.get().getVolume() < volume) {
            throw new TradeException(INSUFFICIENT_COINS);
        } else {
            decreaseHoldingCoin(optionalHoldingCoin.get(), volume);
        }

        // 미체결 거래 생성
        OpenTrade openTrade = openTradeRepository.save(OpenTrade.builder()
                .ticker(ticker)
                .tradeType(TradeType.SELL)
                .member(member)
                .orderCash((long) multiple(volume, price, NORMAL_SCALE))
                .price(price)
                .volume(volume)
                .status(Boolean.FALSE)
                .build());

        // 미체결 거래 등록
        cryptoPrice.addTrade(openTrade.getTicker(), openTrade.getId(), openTrade.getTradeType(), openTrade.getPrice());
    }

    @Transactional
    public void decreaseHoldingCoin(HoldingCoin holdingCoin, Double volume) {
        holdingCoin.decreaseVolume(volume);
        if (holdingCoin.getVolume() <= 0) {
            holdingCoinRepository.delete(holdingCoin);
        }
    }
}
