package com.found_404.funco.follow.domain.repository;

import com.found_404.funco.follow.domain.Follow;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.follow.domain.FollowTrade;

import java.util.List;

public interface FollowTradeRepository extends JpaRepository<FollowTrade, Long> {
    List<FollowTrade> findByFollow(Pageable pageable, Follow follow);
}
