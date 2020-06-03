package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.ResultFootball;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ResultFootballRepository extends JpaRepository<ResultFootball, Long> {

    @Query(value = "SELECT * FROM results_football JOIN games ON results_football.game_id = games.id WHERE games.competition_id = ?1 AND status = 3", nativeQuery = true)
    Set<ResultFootball> findAllResultsForCompetition(Long competitionId);

    ResultFootball findByGameUniqueId(String uniqueId);

}
