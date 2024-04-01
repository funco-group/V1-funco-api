package com.found_404.funco.follow.service;

import com.found_404.funco.follow.domain.Follow;
import com.found_404.funco.follow.domain.FollowingCoin;
import com.found_404.funco.follow.domain.repository.FollowRepository;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.found_404.funco.global.util.DecimalCalculator.divide;
import static com.found_404.funco.global.util.DecimalCalculator.multiple;
import static com.found_404.funco.global.util.ScaleType.*;

@Service
@RequiredArgsConstructor
public class FollowTradeService {
    private final TradeRepository tradeRepository;
    private final FollowRepository followRepository;
    private final HoldingCoinRepository holdingCoinRepository;
    private final FollowingCoinRepository followingCoinRepository;

    @Async
    public void followTrade(Trade trade) {
        List<Follow> followerList = followRepository.findAllByFollowingAndSettled(trade.getMember(), Boolean.FALSE);

        tradeRepository.saveAll(followerList.stream()
                .map(follow -> getTrade(trade, follow))
                .toList());

        System.out.println("비동기 함수 끝!!!!");
    }

    @Transactional // 내부 호출 트랜잭셔널 수정 생각
    public Trade getTrade(Trade trade, Follow follow) {
        Member following = follow.getFollowing();
        Member follower = follow.getFollower();

        double volume;
        long orderCash;

        if (trade.getTradeType().equals(TradeType.BUY)) {
            // 현금에서 얼마를 썼냐 비율
            long prevCash = following.getCash() + trade.getOrderCash();
            double ratio = divide(trade.getOrderCash(), prevCash, NORMAL_SCALE);

            System.out.println("현금에서 산 비중 :" + ratio);

            orderCash = (long) multiple(follow.getCash(), ratio, NORMAL_SCALE);
            volume = divide(orderCash, trade.getPrice(), VOLUME_SCALE);

            System.out.printf("팔로워%d는 투자금액 중 현금 %d원에서 %d원을 사용할거고 %f개를 삽니다.\n",follower.getId(), follow.getCash(), orderCash, volume);
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

            //System.out.printf("%s가 %s를 %f 만큼 팔았다. \n", following.getNickname(), followingCoin.get().getTicker(), ratio);

            FollowingCoin followerCoin = followingCoinRepository.findByFollowAndTicker(follow, trade.getTicker())
                    .orElseThrow(() -> new TradeException(TradeErrorCode.INSUFFICIENT_COINS));

            volume = multiple(followerCoin.getVolume(), ratio, VOLUME_SCALE);
            orderCash = (long) multiple(trade.getPrice(), volume, NORMAL_SCALE);

            System.out.printf("%s는 갖고있는 %f개에서 %f개를 판다. ", follower.getNickname(), followerCoin.getVolume(), volume);
            // 돈 추가
            follow.increaseCash(orderCash);

            // 코인 감소
            followerCoin.decreaseVolume(volume);
            if (followerCoin.getVolume() <= 0) {
                followingCoinRepository.delete(followerCoin);
            }
        }

        return Trade.builder()
                .tradeType(trade.getTradeType())
                .price(trade.getPrice())
                .ticker(trade.getTicker())
                .status(Boolean.TRUE)
                .member(follow.getFollower())
                .volume(volume) // 비율
                .orderCash(orderCash)
                .build();
    }

    @Async
    public void followTrade(List<Trade> trades) {
        trades.forEach(this::followTrade);
    }


}
