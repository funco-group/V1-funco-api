package com.found_404.funco.rank.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.rank.dto.response.RankResponse;
import com.found_404.funco.rank.service.RankService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/rank")
public class RankController {
	private final RankService rankService;

	@GetMapping
	public ResponseEntity<Page<RankResponse>> getRanking(@RequestParam String type,
		@PageableDefault(size = 10) Pageable pageable) {
		return ResponseEntity.ok(rankService.readRanking(type, pageable));
	}

	@GetMapping("/test")
	public ResponseEntity<Void> testRanking() {
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
