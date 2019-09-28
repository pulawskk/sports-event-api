package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, Long> {
}
