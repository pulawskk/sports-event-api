package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import com.pulawskk.sportseventapi.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private GameServiceImpl gameService;

    private TeamServiceImpl teamServiceImpl;

    private Team teamChelsea;
    private Competition premierLeagueCompetition;
    private Set<Competition> competitions = new HashSet<>();
    private Set<Team> teams = new HashSet<>();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        teamChelsea = Team.builder().id(1L).name("Chelsea").build();
        premierLeagueCompetition = Competition.builder().id(1L).name("Premier League").build();

        competitions.add(premierLeagueCompetition);
        teamChelsea.setCompetitions(competitions);

        teams.add(teamChelsea);
        premierLeagueCompetition.setTeams(teams);

        teamServiceImpl = new TeamServiceImpl(teamRepository, gameService);
    }

    @Test
    void shouldReturnTeam_whenTeamWithSpecificIdExist() {
        Optional<Team> optionalChelseaTeam = Optional.of(teamChelsea);
        when(teamRepository.findById(anyLong())).thenReturn(optionalChelseaTeam);

        Team newTeam = teamServiceImpl.findById(1L);

        assertAll(() -> {
            assertThat(newTeam.getId(), is(1L));
            assertThat(newTeam.getName(), is("Chelsea"));
        });
    }

    @Test
    void shouldReturnTeam_whenTeamWithSpecificNameExists() {
        when(teamRepository.findFirstByName(anyString())).thenReturn(Optional.of(teamChelsea));

        Team newTeam = teamServiceImpl.findByName("Chelsea");

        assertAll(() -> {
            assertThat(newTeam.getName(), is("Chelsea"));
            assertThat(newTeam.getId(), is(1L));
        });
    }

    @Test
    void shouldReturnAllTeams_whenTeamsExist() {
        Team arsenalTeam = Team.builder().id(2L).name("Arsenal").competitions(competitions).build();
        premierLeagueCompetition.addNewTeam(arsenalTeam);
        teams.add(arsenalTeam);

        when(teamRepository.findAll()).thenReturn(new ArrayList<Team>(teams));

        Set<Team> teamsFromDb = new HashSet<Team>(teamServiceImpl.findAll());

        assertThat(teamsFromDb, hasSize(2));
    }

    @Test
    void shouldReturnSetOfTeams_whenSpecificCompetitionExists() {
        Team arsenalTeam = Team.builder().id(2L).name("Arsenal").competitions(competitions).build();
        premierLeagueCompetition.addNewTeam(arsenalTeam);
        teams.add(arsenalTeam);

        when(teamRepository.findAllByCompetitions(premierLeagueCompetition)).thenReturn(new ArrayList<Team>(teams));

        Set<Team> teamsFromDb = new HashSet<Team>(teamServiceImpl.findAllByCompetitions(premierLeagueCompetition));

        assertThat(teamsFromDb, hasSize(2));
    }

    @Test
    void shouldReturnSavedTeam_whenTeamIsSaved() {
        Team teamToBeSaved = Team.builder().id(3L).name("Aston Villa").competitions(competitions).build();

        when(teamRepository.save(any())).thenReturn(teamToBeSaved);

        Team savedTeam = teamServiceImpl.save(teamToBeSaved);

        assertThat(savedTeam.getId(), is(3L));
    }

    @Test
    void shouldDeleteTeam_whenTeamWithSpecificIdExists() {
        teamServiceImpl.deleteById(anyLong());
        verify(teamRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteTeam_whenTeamWithSpecificNameExists() {
        teamServiceImpl.delete(any());
        verify(teamRepository,times(1)).delete(any());
    }
}