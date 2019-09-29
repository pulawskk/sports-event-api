package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.OddRepository;
import com.pulawskk.sportseventapi.service.GameService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final OddRepository oddRepository;

    public GameServiceImpl(GameRepository gameRepository, OddRepository oddRepository) {
        this.gameRepository = gameRepository;
        this.oddRepository = oddRepository;
    }

    public Game findGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }


    public Set<Game> findAllByTeamAwayOrTeamHome(Long teamId) {
        return new HashSet<>(gameRepository.findAllByTeamAwayOrTeamHome(teamId));
    }


    public Set<Game> findAllByTeamHome(Team team) {
        return new HashSet<>(gameRepository.findAllByTeamHome(team));
    }


    public Set<Game> findAllByTeamAway(Team team) {
        return new HashSet<>(gameRepository.findAllByTeamAway(team));
    }


    public Game save(Game game) {
        game.getOdds().forEach(odd -> {
            if (oddRepository.findById(odd.getId()) == null) {
                oddRepository.save(odd);
            }
        });

        return gameRepository.save(game);
    }


    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }


    public void delete(Game game) {
        gameRepository.delete(game);
    }
}
