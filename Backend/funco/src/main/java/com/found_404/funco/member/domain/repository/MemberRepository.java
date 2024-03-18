package com.found_404.funco.member.domain.repository;

import com.found_404.funco.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
