package com.found_404.funco.asset.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.asset.dto.response.CashResponse;
import com.found_404.funco.asset.dto.response.CryptoResponse;
import com.found_404.funco.asset.dto.response.HistoryResponse;
import com.found_404.funco.asset.dto.response.TotalAssetResponse;
import com.found_404.funco.asset.service.AssetService;
import com.found_404.funco.global.util.AuthMemberId;
import com.found_404.funco.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/asset")
@RequiredArgsConstructor
public class AssetController {

	private final AssetService assetService;

	@GetMapping()
	public ResponseEntity<TotalAssetResponse> getMemberTotalAsset(@AuthMemberId Long memberId) {
		return ResponseEntity.ok(assetService.getMemberTotalAsset(memberId));
	}

	// 남의 보유자산 확인하기
	@GetMapping("/{memberId}")
	public ResponseEntity<TotalAssetResponse> getOtherMemberTotalAsset(@PathVariable Long memberId) {
		return ResponseEntity.ok(assetService.getMemberTotalAsset(memberId));
	}

	@GetMapping("/cash")
	public ResponseEntity<CashResponse> getMemberCash(@AuthenticationPrincipal Member member) {
		return ResponseEntity.ok(assetService.getMemberCash(member));
	}

	@GetMapping("/crypto/{ticker}")
	public ResponseEntity<CryptoResponse> getCrypto(@AuthenticationPrincipal Member member,
		@PathVariable String ticker) {
		return ResponseEntity.ok(assetService.getCrypto(member, ticker));
	}

	@GetMapping("/history")
	public ResponseEntity<List<HistoryResponse>> getMemberHistory(@AuthenticationPrincipal Member member) {

		return ResponseEntity.ok(assetService.getMemberHistory(member));
	}

}
