package com.found_404.funco.favoritecoin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.favoritecoin.dto.request.FavoriteCoinRequest;
import com.found_404.funco.favoritecoin.dto.response.FavoriteCoinResponse;
import com.found_404.funco.favoritecoin.service.FavoriteCoinService;
import com.found_404.funco.global.util.AuthMemberId;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/crypto/favorite")
public class FavoriteCoinController {

	private final FavoriteCoinService favoriteCoinService;

	@PostMapping
	public ResponseEntity<Void> addFavoriteCoin(@AuthMemberId Long memberId,
		@RequestBody FavoriteCoinRequest favoriteCoinRequest
	) {
		favoriteCoinService.createFavoriteCoin(memberId, favoriteCoinRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<FavoriteCoinResponse> getFavoriteCoin(@AuthMemberId Long memberId
	) {
		return ResponseEntity.ok(favoriteCoinService.readFavoriteCoin(memberId));
	}

	@DeleteMapping("{ticker}")
	public ResponseEntity<Void> removeFavoriteCoin(@AuthMemberId Long memberId, @PathVariable String ticker
	) {
		favoriteCoinService.deleteFavoriteCoin(memberId, ticker);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
