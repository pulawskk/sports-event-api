package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface GameService {

    List<Game> findAll();

    Game findGameById(Long id);

    Game findGameByUniqueId(String uniqueId);

    Set<Game> findAllByTeamAwayOrTeamHome(Long teamId);

    Set<Game> findAllByTeamHome(Team team);

    Set<Game> findAllByTeamAway(Team team);

    Game save(Game game);

    Set<Game> saveAll(Set<Game> games);

    void deleteById(Long id);

    void delete(Game game);

    Set<Game> findAllGeneratedGamesForCompetition(Long competitionId);

    void deleteOldGames(int amountToBeLeft);
}
