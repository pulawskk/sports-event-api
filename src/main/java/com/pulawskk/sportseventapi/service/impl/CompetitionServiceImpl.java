package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.CompetitionRepository;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import com.pulawskk.sportseventapi.service.CompetitionService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CompetitionServiceImpl implements CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final TeamRepository teamRepository;

    public CompetitionServiceImpl(CompetitionRepository competitionRepository, TeamRepository teamRepository) {
        this.competitionRepository = competitionRepository;
        this.teamRepository = teamRepository;
    }

    public Competition findById(Long id) {
        Optional<Competition> competitionFromDb = competitionRepository.findById(id);
        competitionFromDb.ifPresent(competition -> {
            if(competition.getTeams() == null) {
                Set<Team> teams = new HashSet<>(teamRepository.findAllByCompetitions(competition));
                competition.setTeams(teams);
            }
        });
        return competitionFromDb.orElse(null);
    }

    public Competition findByName(String name) {
        Optional<Competition> competitionFromDb = competitionRepository.findByName(name);
        competitionFromDb.ifPresent(competition -> {
            if(competition.getTeams() == null) {
                Set<Team> teams = new HashSet<>(teamRepository.findAllByCompetitions(competition));
                competition.setTeams(teams);
            }
        });
        return competitionFromDb.orElse(null);
    }


    public Set<Competition> findAll() {
        Set<Competition> competitions = new HashSet<>(competitionRepository.findAll());
        competitions.forEach(competition -> {
            if(competition.getTeams() == null) {
                Set<Team> teams = new HashSet<>(teamRepository.findAllByCompetitions(competition));
                competition.setTeams(teams);
            }
        });
        return competitions;
    }


    public Set<Competition> findCompetitionsByTeams(Team team) {
        List<Competition> competitionsFromDb = competitionRepository.findCompetitionsByTeams(team);
        Set<Competition> competitions = new HashSet<>(competitionsFromDb);
        competitions.forEach(competition -> {
            if (competition.getTeams() == null) {
                Set<Team> teams = new HashSet<>(teamRepository.findAllByCompetitions(competition));
                competition.setTeams(teams);
            }
        });
        return competitions;
    }


    public Competition save(Competition competition) {
        if(competition.getTeams() == null || competition.getTeams().size() == 0) {
            //TODO throw exception, log info
            competition.setTeams(new HashSet<>(teamRepository.findAllByCompetitions(competition)));
        }
        return competitionRepository.save(competition);
    }


    public void deleteById(Long id) {
        competitionRepository.deleteById(id);
    }

    public void delete(Competition competition) {
        competitionRepository.delete(competition);
    }

    @Override
    public void deleteAll() {
        competitionRepository.deleteAll();
    }

    public void flush() {
        competitionRepository.flush();
    }
}
