package com.found_404.funco.member.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.auth.dto.OauthId;
import com.found_404.funco.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {
	Optional<Member> findByOauthId(OauthId oauthId);

	boolean existsByNickname(String nickname);
}
