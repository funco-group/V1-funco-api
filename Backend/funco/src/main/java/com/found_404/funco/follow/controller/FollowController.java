package com.found_404.funco.follow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.follow.dto.request.FollowingRequest;
import com.found_404.funco.follow.dto.response.FollowerListResponse;
import com.found_404.funco.follow.dto.response.FollowingListResponse;
import com.found_404.funco.follow.service.FollowService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/v1/follows")
@RequiredArgsConstructor
@RestController
public class FollowController {

	private final FollowService followService;

	@PostMapping
	public ResponseEntity<Void> addFollow(@RequestBody @Valid FollowingRequest request) {
		Long memberID = 3L;
		followService.createFollow(request, memberID);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{followId}")
	public ResponseEntity<Void> removeFollow(@PathVariable Long followId) {
		followService.deleteFollow(followId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@GetMapping("/following")
	public ResponseEntity<FollowingListResponse> getFollowingList(
		@RequestParam(required = false) Long lastFollowId) {
		Long memberId = 3L;
		return ResponseEntity.status(HttpStatus.OK).body(followService.readFollowingList(memberId, lastFollowId));
	}

	@GetMapping("/follower")
	public ResponseEntity<FollowerListResponse> getFollowerList(@RequestParam String settled,
		@RequestParam(required = false) Long lastFollowId) {
		Long memberId = 2L;
		return ResponseEntity.status(HttpStatus.OK)
			.body(followService.readFollowerList(memberId, settled, lastFollowId));
	}
}
