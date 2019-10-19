package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.OddRepository;
import com.pulawskk.sportseventapi.service.GameService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Set<Game> findAllGeneratedGamesForCompetition(Long competitionId) {
        return gameRepository.findAllGeneratedGames(competitionId).stream().collect(Collectors.toSet());
    }

    public List<JSONObject> generateGames(Long competitionId) throws JSONException {
        Set<Game> currentGamesFromDb = findAllGeneratedGamesForCompetition(competitionId);
        List<JSONObject> generatedGames = new ArrayList<>();

        if(currentGamesFromDb != null) {
            currentGamesFromDb.forEach(game -> {
                generatedGames.add(generateJsonFromGame(game));
            });
        }
        JSONObject jsonInfo = new JSONObject();
        jsonInfo.put("gamesNumber", currentGamesFromDb.size());
        generatedGames.add(jsonInfo);
        return generatedGames;
    }

    public JSONObject generateJsonFromGame(Game game) {
        JSONObject jsonFromGame = new JSONObject();
        JSONObject json = new JSONObject();
        json.put("teamHome", game.getTeamHome().getName());
        json.put("teamAway", game.getTeamAway().getName());
        json.put("odds", game.getOdds());
        json.put("gameStatus", game.getStatus().name());
        Optional.ofNullable(game.getStartDate()).ifPresent(date -> json.put("startGame", date));
        Optional.ofNullable(game.getEndDate()).ifPresent(date -> json.put("endGame", date));
        return jsonFromGame;
    }
}