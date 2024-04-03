package com.found_404.funco.member.domain;

import org.hibernate.annotations.Comment;

import com.found_404.funco.auth.dto.OauthId;
import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.global.util.DecimalCalculator;
import com.found_404.funco.global.util.ScaleType;
import com.found_404.funco.member.domain.type.MemberStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Comment("OAuth 코드")
	@Column(nullable = false)
	private OauthId oauthId;

	@Comment("닉네임")
	@Column(length = 20)
	private String nickname;

	@Comment("프로필 이미지 URL")
	@Column(length = 2100)
	private String profileUrl;

	@Comment("한 줄 소개")
	@Column(length = 100)
	private String introduction;

	@Comment("가용 현금")
	@Column(nullable = false)
	private Long cash;

	@Comment("회원 유형")
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private MemberStatus status;

	private static final Double COMMISSION = 0.05;

	@Builder
	public Member(OauthId oauthId, String nickname, String profileUrl, String introduction, Long cash,
		MemberStatus status) {
		this.oauthId = oauthId;
		this.nickname = nickname;
		this.profileUrl = profileUrl;
		this.introduction = introduction;
		this.cash = cash;
		this.status = status;
	}

	public void decreaseCash(long orderCash) {
		if (this.cash < orderCash) {
			throw new RuntimeException("잔액이 부족합니다.");  // member domain에서 custom exception 추가
		}
		this.cash -= orderCash;
	}

	public void increaseCash(long orderCash) {
		this.cash += getCashWithCommission(orderCash);
	}

	public long getCashWithCommission(long orderCash) {
		return orderCash - (long)(DecimalCalculator.multiple(orderCash, COMMISSION, ScaleType.NORMAL_SCALE));
	}

	public void settleCash(long settlement) {
		this.cash += settlement;
	}

	public void recoverCash(long cash) {
		this.cash += cash;
	}

	public void updateOauthId(OauthId oauthId) {
		this.oauthId = oauthId;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
	}

	public void updateIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public void withdraw() {
		this.status = MemberStatus.WITHDRAW;
	}
}
