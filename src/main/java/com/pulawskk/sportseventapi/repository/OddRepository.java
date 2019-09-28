package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Odd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OddRepository extends JpaRepository<Odd, Long> {
}
