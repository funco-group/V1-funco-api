package com.found_404.funco.trade.controller;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.dto.OpenTradeDto;
import com.found_404.funco.trade.dto.TradeDto;
import com.found_404.funco.trade.dto.request.*;
import com.found_404.funco.trade.dto.response.HoldingCoinsResponse;
import com.found_404.funco.trade.dto.response.MarketTradeResponse;
import com.found_404.funco.trade.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/trade")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    // 시장가 매수
    @PostMapping("/market-buying")
    public ResponseEntity<MarketTradeResponse> marketBuying(@AuthenticationPrincipal Member member,
                                                            @RequestBody @Valid MarketBuyingRequest request) {

        return ResponseEntity.ok(tradeService.marketBuying(member, request.ticker(), request.orderCash()));
    }

    // 시장가 매도
    @PostMapping("/market-selling")
    public ResponseEntity<MarketTradeResponse> marketSelling(@AuthenticationPrincipal Member member,
                                                             @RequestBody @Valid MarketSellingRequest request) {

        return ResponseEntity.ok(tradeService.marketSelling(member, request.ticker(), request.volume()));
    }

    // 지정가 매수
    @PostMapping("/limit-buying")
    public ResponseEntity<?> limitBuying(@AuthenticationPrincipal Member member,
                                         @RequestBody @Valid LimitBuyingRequest request) {

        tradeService.limitBuying(member, request.ticker(), request.price(), request.volume());
        return ResponseEntity.ok().build();
    }

    // 지정가 매도
    @PostMapping("/limit-selling")
    public ResponseEntity<?> limitSelling(@AuthenticationPrincipal Member member,
                                          @RequestBody @Valid LimitSellingRequest request) {

        tradeService.limitSelling(member, request.ticker(), request.price(), request.volume());
        return ResponseEntity.ok().build();
    }

    // 보유 중인 코인 조회
    @GetMapping("/holding")
    public ResponseEntity<HoldingCoinsResponse> getHoldingCoin(@AuthenticationPrincipal Member member) {
        return ResponseEntity.ok(tradeService.getHoldingCoins(member));
    }

    // 체결 코인 거래 내역, param ticker , follow 여부 필수
    @GetMapping("/orders")
    public ResponseEntity<List<TradeDto>> getOrders(@AuthenticationPrincipal Member member,
                                                    Pageable pageable, TradeRequest tradeRequest) {

        return ResponseEntity.ok(tradeService.getOrders(member, tradeRequest.ticker(), pageable));
    }

    // 미체결 거래 내역 보기
    @GetMapping("/open-orders")
    public ResponseEntity<List<OpenTradeDto>> getOpenOrders(@AuthenticationPrincipal Member member,
                                                            Pageable pageable, TradeRequest tradeRequest) {

        return ResponseEntity.ok(tradeService.getOpenOrders(member, tradeRequest.ticker(), pageable));
    }

    // 미체결 거래 취소
    @DeleteMapping("/open-orders/{id}")
    public ResponseEntity<?> getOpenOrders(@AuthenticationPrincipal Member member,
                                           @PathVariable Long id) {

        tradeService.deleteOpenTrade(member, id);
        return ResponseEntity.ok().build();
    }

}
