package com.found_404.funco.trade.controller;

import com.found_404.funco.trade.dto.request.MarketBuyingRequest;
import com.found_404.funco.trade.dto.request.MarketSellingRequest;
import com.found_404.funco.trade.dto.response.HoldingCoinsResponse;
import com.found_404.funco.trade.dto.response.MarketTradeResponse;
import com.found_404.funco.trade.service.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/trade")
@RequiredArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    // 시장가 매수
    @PostMapping("/trade/market-buying")
    public ResponseEntity<MarketTradeResponse> marketBuying(@RequestBody @Valid MarketBuyingRequest request) {
        final long memberId = 1L;

        return ResponseEntity.ok(tradeService.marketBuying(memberId, request.ticker(), request.orderCash()));
    }

    // 시장가 매도
    @PostMapping("/trade/market-selling")
    public ResponseEntity<MarketTradeResponse> marketSelling(@RequestBody @Valid MarketSellingRequest request) {
        final long memberId = 1L;

        return ResponseEntity.ok(tradeService.marketSelling(memberId, request.ticker(), request.volume()));
    }

    // 지정가 매수

    // 지정가 매도

    // 보유 중인 코인 조회
    @GetMapping("/crypto/holding")
    public ResponseEntity<HoldingCoinsResponse> getHoldingCoin() {
        final Long memberId = 1L;

        return ResponseEntity.ok(tradeService.getHoldingCoins(memberId));
    }

    // 특정 체결 코인 거래 내역

    // 미체결 거래 내역 보기

    // 미체결 거래 취소

}
