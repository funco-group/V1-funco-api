package com.found_404.funco.follow.service;

import com.found_404.funco.follow.domain.Follow;
import com.found_404.funco.follow.domain.FollowTrade;
import com.found_404.funco.follow.domain.FollowingCoin;
import com.found_404.funco.follow.domain.repository.FollowRepository;
import com.found_404.funco.follow.domain.repository.FollowTradeRepository;
import com.found_404.funco.follow.domain.repository.FollowingCoinRepository;
import com.found_404.funco.global.util.DecimalCalculator;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.HoldingCoinRepository;
import com.found_404.funco.trade.domain.repository.TradeRepository;
import com.found_404.funco.trade.domain.type.TradeType;
import com.found_404.funco.trade.exception.TradeErrorCode;
import com.found_404.funco.trade.exception.TradeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.found_404.funco.global.util.DecimalCalculator.divide;
import static com.found_404.funco.global.util.DecimalCalculator.multiple;
import static com.found_404.funco.global.util.ScaleType.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowTradeService {
    private final FollowRepository followRepository;
    private final HoldingCoinRepository holdingCoinRepository;
    private final FollowingCoinRepository followingCoinRepository;
    private final FollowTradeRepository followTradeRepository;

    @Async
    public void followTrade(Trade trade) {
        List<Follow> followerList = followRepository.findAllByFollowingAndSettled(trade.getMember(), Boolean.FALSE);

        followTradeRepository.saveAll(followerList.stream()
                .map(follow -> getTrade(trade, follow))
                .toList());
    }

    public FollowTrade getTrade(Trade trade, Follow follow) {
        Member following = follow.getFollowing();
        Member follower = follow.getFollower();

        double volume;
        long orderCash;

        if (trade.getTradeType().equals(TradeType.BUY)) {
            // 부모가 현금에서 얼마를 썼냐 비율 => 캡쳐링 필요
            long prevCash = following.getCash() + trade.getOrderCash();
            double ratio = divide(trade.getOrderCash(), prevCash, NORMAL_SCALE);

            orderCash = (long) multiple(follow.getCash(), ratio, CASH_SCALE);
            volume = divide(orderCash, trade.getPrice(), VOLUME_SCALE);

            // 돈 쓰기
            follow.decreaseCash(orderCash);

            // 코인 추가
            Optional<FollowingCoin> followerCoin = followingCoinRepository.findByFollowAndTicker(follow, trade.getTicker());
            if (followerCoin.isEmpty()) {
                followingCoinRepository.save(FollowingCoin.builder()
                        .ticker(trade.getTicker())
                        .averagePrice(trade.getPrice())
                        .volume(volume)
                        .follow(follow)
                        .build());
            } else {
                followerCoin.get().increaseVolume(volume, trade.getPrice());
            }
        } else {
            // 코인에서 얼마를 팔았냐 비율
            Optional<HoldingCoin> followingCoin = holdingCoinRepository.findByMemberAndTicker(following, trade.getTicker());

            double prevVolume = trade.getVolume() + (followingCoin.isEmpty() ? 0 : followingCoin.get().getVolume());
            double ratio = DecimalCalculator.divide(trade.getVolume(), prevVolume, NORMAL_SCALE);

            FollowingCoin followerCoin = followingCoinRepository.findByFollowAndTicker(follow, trade.getTicker())
                    .orElseThrow(() -> new TradeException(TradeErrorCode.INSUFFICIENT_COINS));

            volume = multiple(followerCoin.getVolume(), ratio, VOLUME_SCALE);
            orderCash = (long) multiple(trade.getPrice(), volume, NORMAL_SCALE);

            // 돈 추가
            follow.increaseCash(orderCash);

            // 코인 감소
            followerCoin.decreaseVolume(volume);
            if (followerCoin.getVolume() <= 0) {
                followingCoinRepository.delete(followerCoin);
            }
        }

        log.info("[{}] member: {} -> follwer: {}, {}가 {}원에 {}만큼 {}원어치 거래 체결.", LocalDateTime.now(), following.getNickname(), follower.getNickname(),
                trade.getTicker(), trade.getPrice(), volume, orderCash);

        return FollowTrade.builder()
                .follow(follow)
                .tradeType(trade.getTradeType())
                .price(trade.getPrice())
                .ticker(trade.getTicker())
                .volume(volume) // 비율
                .orderCash(orderCash)
                .build();
    }

    @Async
    public void followTrade(List<Trade> trades) {
        trades.forEach(this::followTrade);
    }


}
