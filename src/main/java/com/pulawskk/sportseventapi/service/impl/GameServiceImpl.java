package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.service.GameService;
import com.pulawskk.sportseventapi.service.OddService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final OddService oddService;
    private final ResultFootballService resultFootballService;

    public GameServiceImpl(GameRepository gameRepository, OddService oddService, ResultFootballService resultFootballService) {
        this.gameRepository = gameRepository;
        this.oddService = oddService;
        this.resultFootballService = resultFootballService;
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
            if (oddService.findById(odd.getId()) == null) {
                oddService.save(odd);
            }
        });

        return gameRepository.save(game);
    }


    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }


    public void delete(Game game) {
        Set<Odd> oddsToDelete = game.getOdds();
        for (Odd odd : oddsToDelete) {
            oddService.delete(odd);
        }
        ResultFootball gameResultFootballToDelete = game.getResultFootball();
        if (gameResultFootballToDelete != null) {
            resultFootballService.delete(gameResultFootballToDelete);
        }

        gameRepository.delete(game);
    }

    @Override
    public void flush() {
        gameRepository.flush();
    }
}
