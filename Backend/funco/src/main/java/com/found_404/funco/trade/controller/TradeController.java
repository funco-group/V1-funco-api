package com.found_404.funco.trade.controller;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/trade")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    // 시장가 매수
    @PostMapping("/market-buying")
    public ResponseEntity<MarketTradeResponse> marketBuying(@RequestBody @Valid MarketBuyingRequest request) {
        final long memberId = 1L;

        return ResponseEntity.ok(tradeService.marketBuying(memberId, request.ticker(), request.orderCash()));
    }

    // 시장가 매도
    @PostMapping("/market-selling")
    public ResponseEntity<MarketTradeResponse> marketSelling(@RequestBody @Valid MarketSellingRequest request) {
        final long memberId = 1L;

        return ResponseEntity.ok(tradeService.marketSelling(memberId, request.ticker(), request.volume()));
    }

    // 지정가 매수
    @PostMapping("/limit-buying")
    public ResponseEntity<?> limitBuying(@RequestBody @Valid LimitBuyingRequest request) {
        final long memberId = 1L;

        tradeService.limitBuying(memberId, request.ticker(), request.price(), request.volume());
        return ResponseEntity.ok().build();
    }

    // 지정가 매도
    @PostMapping("/limit-selling")
    public ResponseEntity<?> limitSelling(@RequestBody @Valid LimitSellingRequest request) {
        final long memberId = 1L;

        tradeService.limitSelling(memberId, request.ticker(), request.price(), request.volume());
        return ResponseEntity.ok().build();
    }

    // 보유 중인 코인 조회
    @GetMapping("/holding")
    public ResponseEntity<HoldingCoinsResponse> getHoldingCoin() {
        final Long memberId = 1L;

        return ResponseEntity.ok(tradeService.getHoldingCoins(memberId));
    }

    // 체결 코인 거래 내역, param ticker , follow 여부 필수
    @GetMapping("/orders")
    public ResponseEntity<List<TradeDto>> getOrders(Pageable pageable, TradeRequest tradeRequest) {
        final Long memberId = 1L;

        return ResponseEntity.ok(tradeService.getOrders(memberId, tradeRequest.ticker(), tradeRequest.follow(), pageable));
    }

    // 미체결 거래 내역 보기
    @GetMapping("/open-orders")
    public ResponseEntity<List<OpenTradeDto>> getOpenOrders(Pageable pageable, TradeRequest tradeRequest) {
        final Long memberId = 1L;

        return ResponseEntity.ok(tradeService.getOpenOrders(memberId, tradeRequest.follow(), tradeRequest.ticker(), pageable));
    }

    // 미체결 거래 취소
    @DeleteMapping("/open-orders/{id}")
    public ResponseEntity<?> getOpenOrders(@PathVariable Long id) {
        final Long memberId = 1L;

        tradeService.deleteOpenTrade(memberId, id);
        return ResponseEntity.ok().build();
    }

}
