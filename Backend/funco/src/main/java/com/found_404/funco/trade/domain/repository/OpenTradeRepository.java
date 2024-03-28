package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.trade.domain.OpenTrade;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OpenTradeRepository extends JpaRepository<OpenTrade, Long>, QueryDslOpenTradeRepository {

    @EntityGraph(attributePaths = {"member"})
    List<OpenTrade> findAllByIdIn(Collection<Long> id);
}
