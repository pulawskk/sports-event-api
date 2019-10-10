package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import com.pulawskk.sportseventapi.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;

    public TeamServiceImpl(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }


    public Team findById(Long id) {
        Optional<Team> teamFromDb = teamRepository.findById(id);
        return teamFromDb.orElse(null);
    }

    public Team findByName(String name) {
        Optional<Team> teamFromDb = teamRepository.findByName(name);
        return teamFromDb.orElse(null);
    }

    public Set<Team> findAll() {
        List<Team> teamsFromDb = teamRepository.findAll();
        return new HashSet<>(teamsFromDb);
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
}
