package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface GameService {

    Game findGameById(Long id);

    Set<Game> findAllByTeamAwayOrTeamHome(Long teamId);

    Set<Game> findAllByTeamHome(Team team);

    Set<Game> findAllByTeamAway(Team team);

    Game save(Game game);

    void deleteById(Long id);

    void delete(Game game);

    void flush();
}
