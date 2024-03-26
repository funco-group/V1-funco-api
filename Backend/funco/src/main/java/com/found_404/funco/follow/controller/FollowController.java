package com.found_404.funco.follow.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.follow.dto.request.FollowingRequest;
import com.found_404.funco.follow.service.FollowService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/follows")
@RequiredArgsConstructor
@RestController
public class FollowController {

	private final FollowService followService;

	@PostMapping
	public ResponseEntity<Void> addFollow(@RequestBody @Valid FollowingRequest request) {
		Long memberID = 2L;
		followService.createFollow(request, memberID);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{followId}")
	public ResponseEntity<Void> removeFollow(@PathVariable Long followId) {
		followService.deleteFollow(followId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
