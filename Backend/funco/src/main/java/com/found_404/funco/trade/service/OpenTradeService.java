package com.found_404.funco.trade.service;

import com.found_404.funco.follow.service.FollowTradeService;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.notification.domain.type.NotificationType;
import com.found_404.funco.notification.service.NotificationService;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.OpenTrade;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;
import com.found_404.funco.trade.domain.repository.OpenTradeRepository;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import com.found_404.funco.trade.domain.type.TradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OpenTradeService {
    private final TradeRepository tradeRepository;
    private final HoldingCoinRepository holdingCoinRepository;
    private final OpenTradeRepository openTradeRepository;
    private final FollowTradeService followTradeService;
    private final NotificationService notificationService;
    private final MemberRepository memberRepository;

    @Async
    public void processTrade(List<Long> concludingTradeIds, Long tradePrice) {
//        if (removeTicker) {
//            cryptoPrice.removeTicker(ticker);
//        } 나중에 최적화

        // 거래 처리할 미체결 거래 가져오기
        List<OpenTrade> openTrades = openTradeRepository.findAllByIdIn(concludingTradeIds);

        // 체결 데이터로 전환
        List<Trade> trades = openTrades.stream()
            .map(openTrade -> OpenTrade.toTrade(openTrade, tradePrice))
            .toList();

        // 체결 데이터 저장
        tradeRepository.saveAll(trades);

        // 자산 업데이트
        for (int i = 0; i < trades.size(); i++) {
            processAsset(trades.get(i), Math.abs(openTrades.get(i).getOrderCash() - trades.get(i).getOrderCash()));
        }

        // 미체결 데이터 삭제
        openTradeRepository.deleteAll(openTrades);

        // 알림
        for (Trade trade : trades) {
            notificationService.sendNotification(trade.getMember().getId(), trade.getTradeType().equals(TradeType.BUY) ? NotificationType.BUY : NotificationType.SELL
                    , getMessage(trade));
        }

        // 팔로우 구매
        followTradeService.followTrade(trades);
    }

    private String getMessage(Trade trade) {
        StringBuilder message = new StringBuilder();
        message.append("[").append(trade.getTicker()).append("] ")
                        .append(String.format("%,f", trade.getVolume())).append("개 ")
                        .append(String.format("%,d", trade.getPrice())).append("원 ")
                        .append(trade.getTradeType().getKorean()).append(" 체결 ");
        return message.toString();
    }

    private void processAsset(Trade trade, Long recoverCash) {
        if (trade.getTradeType().equals(TradeType.BUY)) { // BUY
            Optional<HoldingCoin> optionalHoldingCoin = holdingCoinRepository.findByMemberAndTicker(trade.getMember(), trade.getTicker());
            HoldingCoin holdingCoin;
            if (optionalHoldingCoin.isPresent()) {
                holdingCoin = optionalHoldingCoin.get();
                holdingCoin.increaseVolume(trade.getVolume(), trade.getPrice());
            } else {
                holdingCoin = HoldingCoin.builder()
                        .member(trade.getMember())
                        .volume(trade.getVolume())
                        .averagePrice(trade.getPrice())
                        .ticker(trade.getTicker())
                        .build();
            }

            holdingCoinRepository.save(holdingCoin);
        } else { // SELL
            trade.getMember().increaseCash(trade.getOrderCash());
            memberRepository.save(trade.getMember());
        }

        trade.getMember().recoverCash(recoverCash); // 거래 금액 대비 차액 입금
    }

}
