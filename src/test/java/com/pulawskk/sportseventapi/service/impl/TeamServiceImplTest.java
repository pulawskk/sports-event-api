package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.CompetitionRepository;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

//@ExtendWith(MockitoExtension.class)
class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private CompetitionRepository competitionRepository;

    private TeamServiceImpl teamServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Team teamChelsea = Team.builder().id(1L).name("Chelsea").build();
        Competition premierLeagueCompetition = Competition.builder().id(1L).name("Premier League").build();
        Set<Competition> competitions = new HashSet<>();
        competitions.add(premierLeagueCompetition);
        teamChelsea.setCompetitions(competitions);
        Set<Team> teams = new HashSet<>();
        teams.add(teamChelsea);
        premierLeagueCompetition.setTeams(teams);
    }

    @Test
    void getTeamById() {

    }

}