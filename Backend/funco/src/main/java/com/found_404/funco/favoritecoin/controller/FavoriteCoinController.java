package com.found_404.funco.favoritecoin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.favoritecoin.dto.request.FavoriteCoinRequest;
import com.found_404.funco.favoritecoin.dto.response.FavoriteCoinResponse;
import com.found_404.funco.favoritecoin.service.FavoriteCoinService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/crypto/favorite")
public class FavoriteCoinController {

	private final FavoriteCoinService favoriteCoinService;

	@PostMapping
	public ResponseEntity<Void> addFavoriteCoin(
		// 멤버 추가할 예정
		@RequestBody FavoriteCoinRequest favoriteCoinRequest
	) {
		favoriteCoinService.createFavoriteCoin(1L, favoriteCoinRequest);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@GetMapping
	public ResponseEntity<FavoriteCoinResponse> getFavoriteCoin(
		// 멤버 추가할 예정
	) {
		return ResponseEntity.ok(favoriteCoinService.readFavoriteCoin(1L));
	}

	@DeleteMapping
	public ResponseEntity<Void> removeFavoriteCoin(
		// 멤버 추가할 예정
		@RequestBody FavoriteCoinRequest favoriteCoinRequest
	) {
		favoriteCoinService.deleteFavoriteCoin(1L, favoriteCoinRequest);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
