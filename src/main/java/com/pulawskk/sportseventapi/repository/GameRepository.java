package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
}
