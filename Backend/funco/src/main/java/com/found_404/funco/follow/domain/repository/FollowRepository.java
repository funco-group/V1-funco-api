package com.found_404.funco.follow.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.follow.domain.Follow;
import com.found_404.funco.member.domain.Member;

public interface FollowRepository extends JpaRepository<Follow, Long>, QueryDslFollowRepository {
	Optional<Follow> findByFollowingAndFollower(Member following, Member follower);
}
