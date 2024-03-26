package com.found_404.funco.follow.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.follow.domain.Follow;
import com.found_404.funco.follow.domain.FollowingCoin;

public interface FollowingCoinRepository extends JpaRepository<FollowingCoin, Long> {
	List<FollowingCoin> findFollowingCoinsByFollow(Follow follow);
}
