package com.found_404.funco.rank.controller;

import java.util.List;

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
@RequestMapping("/rank")
public class RankController {
	private final RankService rankService;

	@GetMapping
	public ResponseEntity<List<RankResponse>> getRanking(@RequestParam String type) {
		return ResponseEntity.ok(rankService.readRanking(type));
	}
}
