package com.pulawskk.sportseventapi.repository;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT * FROM games WHERE team_away_id = ?1 OR team_home_id = ?1", nativeQuery = true)
    List<Game> findAllByTeamAwayOrTeamHome(Long teamId);

    List<Game> findAllByTeamAway(Team team);

    List<Game> findAllByTeamHome(Team team);


}
