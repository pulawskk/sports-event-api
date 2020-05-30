package com.pulawskk.sportseventapi.service;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface CompetitionService {

    Competition findById(Long id);

    Competition findByName(String name);

    Set<Competition> findAll();

    Set<Competition> findCompetitionsByTeam(Team team);

    Competition save(Competition competition);

    void deleteById(Long id);

    void delete(Competition competition);
}
