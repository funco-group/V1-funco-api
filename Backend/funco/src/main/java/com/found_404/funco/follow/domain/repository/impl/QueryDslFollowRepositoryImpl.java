package com.found_404.funco.follow.domain.repository.impl;

import com.found_404.funco.follow.domain.repository.QueryDslFollowRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslFollowRepositoryImpl implements QueryDslFollowRepository {
	private final JPAQueryFactory jpaQueryFactory;
}
