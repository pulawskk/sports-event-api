package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import com.pulawskk.sportseventapi.service.TeamService;
import org.springframework.stereotype.Service;
import org.testcontainers.shaded.com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final GameServiceImpl gameService;

    public TeamServiceImpl(TeamRepository teamRepository, GameServiceImpl gameService) {
        this.teamRepository = teamRepository;
        this.gameService = gameService;
    }


    public Team findById(Long id) {
        Optional<Team> teamFromDb = teamRepository.findById(id);
        return teamFromDb.orElse(null);
    }

    public Team findByName(String name) {
        Optional<Team> teamFromDb = teamRepository.findFirstByName(name);
        return teamFromDb.orElse(null);
    }

    public Set<Team> findAll() {
        List<Team> teamsFromDb = teamRepository.findAll();
        Set<Team> teams = new HashSet<>();
        if (teamsFromDb != null) {
            return teamsFromDb.stream().collect(Collectors.toSet());
        }
        return teams;
    }

    public Set<Team> findAllByCompetitions(Competition competition) {
        List<Team> teamsFromDb = teamRepository.findAllByCompetitions(competition);
        return teamsFromDb.stream().collect(Collectors.toSet());
    }

    public Team save(Team team) {
        return teamRepository.save(team);
    }

    public void deleteById(Long id) {
        teamRepository.deleteById(id);
    }

    public void delete(Team team) {
        teamRepository.delete(team);
    }

    public void flush(){
        teamRepository.flush();
    }

    public void deleteAll() {
        Set<Team> teams = teamRepository.findAll().stream().collect(Collectors.toSet());
        for(Team team : teams) {
            team.setCompetitions(null);
//            Set<Game> gamesAway = gameService.findAllByTeamAway(team).stream().collect(Collectors.toSet());
//            for(Game game : gamesAway) {
//                game.setTeamAway(null);
//            }
//            Set<Game> gamesHome = gameService.findAllByTeamHome(team).stream().collect(Collectors.toSet());
//            for(Game game : gamesHome) {
//                game.setTeamHome(null);
//            }
            Set<Game> games = gameService.findAllByTeamAwayOrTeamHome(team.getId()).stream().collect(Collectors.toSet());
            for(Game game : games) {
                game.setTeamAway(null);
                game.setTeamHome(null);
                gameService.delete(game);
            }
            teamRepository.deleteById(team.getId());
        }

        teams = teamRepository.findAll().stream().collect(Collectors.toSet());
    }
}
