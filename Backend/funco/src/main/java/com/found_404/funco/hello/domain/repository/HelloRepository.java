package com.found_404.funco.hello.domain.repository;

import com.found_404.funco.hello.domain.Hello;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HelloRepository extends JpaRepository<Hello, Long> {

}
