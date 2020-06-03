package com.pulawskk.sportseventapi.service.impl;

import com.google.common.collect.Lists;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.OddRepository;
import com.pulawskk.sportseventapi.service.GameService;
import com.pulawskk.sportseventapi.service.JsonUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameServiceImpl implements GameService, JsonUtil {

    private final GameRepository gameRepository;
    private final OddRepository oddRepository;

    public GameServiceImpl(GameRepository gameRepository, OddRepository oddRepository) {
        this.gameRepository = gameRepository;
        this.oddRepository = oddRepository;
    }

    @Override
    public List<Game> findAll() {
        return gameRepository.findAll();
    }

    public Game findGameById(Long id) {
        return gameRepository.findById(id).orElse(null);
    }

    @Override
    public Game findGameByUniqueId(String uniqueId) {
        Game game = gameRepository.findGameByUniqueId(uniqueId);
        if (game != null) {
            return game;
        }
        return Game.builder().build();
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
        return gameRepository.save(game);
    }

    @Override
    public Set<Game> saveAll(Set<Game> games) {
        return gameRepository.saveAll(games).stream().collect(Collectors.toSet());
    }


    public void deleteById(Long id) {
        gameRepository.deleteById(id);
    }


    public void delete(Game game) {
        gameRepository.delete(game);
    }

    @Override
    @Transactional
    public Set<Game> findAllGeneratedGamesForCompetition(Long competitionId) {
        return new HashSet<>(gameRepository.findAllGeneratedGames(competitionId));
    }

    @Override
    public void deleteOldGames(int amountToBeLeft) {
        List<Game> gamesToBeDeleted = gameRepository.findAllGamesToBeDeleted(amountToBeLeft);
        gameRepository.deleteAll(gamesToBeDeleted);
    }

    @Transactional
    public List<Game> generateInplayGamesForCompetition(Long competitionId) throws JSONException {
        Set<Game> currentGamesFromDb = findAllGeneratedGamesForCompetition(competitionId);
        List<JSONObject> generatedGames = new ArrayList<>();

        if(currentGamesFromDb.size() > 0) {
            currentGamesFromDb.forEach(game -> {
                generatedGames.add(generateJsonFromGame(game));
            });
        }

        return Lists.newArrayList(currentGamesFromDb);

//        JSONObject jsonInfo = new JSONObject();
//        jsonInfo.put("gamesNumber", currentGamesFromDb.size());
//        generatedGames.add(jsonInfo);
//        return generatedGames;
    }
}