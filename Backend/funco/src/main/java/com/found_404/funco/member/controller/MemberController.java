package com.found_404.funco.member.controller;

import com.found_404.funco.member.dto.RequestIntroduction;
import com.found_404.funco.member.dto.RequestNickName;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

	// 닉네임 변경
	@PatchMapping("/nickname")
	public ResponseEntity<?> updateNickname(@AuthMemberId Long loginMemberId,
											@RequestBody @Valid RequestNickName requestNickName) {
		memberService.updateNickname(loginMemberId, requestNickName.nickname());
		return ResponseEntity.ok().build();
	}

	// 소개 수정
	@PatchMapping("/nickname")
	public ResponseEntity<?> updateNickname(@AuthMemberId Long loginMemberId,
											@RequestBody @Valid RequestIntroduction requestIntroduction) {
		memberService.updateIntroduce(loginMemberId, requestIntroduction.introduction());
		return ResponseEntity.ok().build();
	}

	// 회원 탈퇴
	@PatchMapping("/withdraw")
	public ResponseEntity<?> withdraw(@AuthMemberId Long loginMemberId) {
		memberService.withdraw(loginMemberId);
		return ResponseEntity.ok().build();
	}
}
