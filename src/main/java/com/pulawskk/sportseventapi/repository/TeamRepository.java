package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
