package com.found_404.funco.asset.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.asset.dto.response.CashResponse;
import com.found_404.funco.asset.dto.response.CryptoResponse;
import com.found_404.funco.asset.dto.response.TotalAssetResponse;
import com.found_404.funco.asset.service.AssetService;
import com.found_404.funco.member.domain.Member;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/v1/asset")
@RequiredArgsConstructor
public class AssetController {

	private final AssetService assetService;

	@GetMapping()
	public ResponseEntity<TotalAssetResponse> getMemberTotalAsset(@AuthenticationPrincipal Member member) {
		return ResponseEntity.ok(assetService.getMemberTotalAsset(member));
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

}
