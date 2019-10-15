package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface TeamService {

    Team findById(Long id);

    Team findByName(String name);

    Set<Team> findAll();

    Set<Team> findAllByCompetitions(Competition competition);

    Team save(Team team);

    void deleteById(Long id);

    void delete(Team team);

    void deleteAll();
}
