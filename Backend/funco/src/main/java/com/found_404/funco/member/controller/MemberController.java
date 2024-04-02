package com.found_404.funco.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.global.util.AuthMemberId;
import com.found_404.funco.member.dto.response.MemberResponse;
import com.found_404.funco.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/members")
public class MemberController {
	private final MemberService memberService;

	// 마이 페이지 조회
	@GetMapping("/{memberId}")
	public ResponseEntity<MemberResponse> getMember(@AuthMemberId Long loginMemberId,
		@PathVariable Long memberId
	) {
		return ResponseEntity.ok(memberService.readMember(loginMemberId, memberId));
	}
}
