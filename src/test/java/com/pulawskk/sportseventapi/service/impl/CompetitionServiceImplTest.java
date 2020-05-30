package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.repository.CompetitionRepository;
import com.pulawskk.sportseventapi.repository.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class CompetitionServiceImplTest {

    @Mock
    private CompetitionRepository competitionRepository;

    @Mock
    private TeamRepository teamRepository;

    private CompetitionServiceImpl competitionServiceImpl;

    private Competition competitionPremierLeague;
    private Set<Team> teams;
    private Set<Competition> competitions = new HashSet<>();
    private Team chelseaTeam;
    private Team arsenalTeam;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        competitionServiceImpl = new CompetitionServiceImpl(competitionRepository, teamRepository);

        competitionPremierLeague = Competition.builder().id(1L).name("Premier League").build();
        chelseaTeam = Team.builder().id(1L).name("Chelsea").build();
        arsenalTeam = Team.builder().id(2L).name("Arsenal").build();
        chelseaTeam.addCompetition(competitionPremierLeague);
        arsenalTeam.addCompetition(competitionPremierLeague);
        Set<Team> teams = new HashSet<>();
        teams.add(chelseaTeam);
        teams.add(arsenalTeam);
        competitions.add(competitionPremierLeague);
    }

    @Test
    void shouldReturnCompetition_whenCompetitionWithSpecificIdExists() {
        when(competitionRepository.findById(anyLong())).thenReturn(Optional.of(competitionPremierLeague));

        Competition newCompetition = competitionServiceImpl.findById(1L);

        assertAll(() -> {
            assertThat(newCompetition.getId(), is(1L));
            assertThat(newCompetition.getName(), is("Premier League"));
        });
    }

    @Test
    void shouldReturnCompetition_whenCompetitionWithSpecificNameExists() {
        when(competitionRepository.findByName(anyString())).thenReturn(Optional.of(competitionPremierLeague));

        Competition newCompetition = competitionServiceImpl.findByName("Premier League");

        assertAll(() -> {
            assertThat(newCompetition.getId(), is(1L));
            assertThat(newCompetition.getName(), is("Premier League"));
        });
    }

    @Test
    void shouldReturnAllCompetitions_whenCompetitionsExist() {
        Competition competitionFaCup = Competition.builder().id(2L).name("FA Cup").teams(teams).build();
        competitions.add(competitionFaCup);

        when(competitionRepository.findAll()).thenReturn(new ArrayList<>(competitions));

        Set<Competition> competitionsFromDb = competitionServiceImpl.findAll();

        assertThat(competitionsFromDb, hasSize(2));
    }

    @Test
    void shouldReturnAllCompetitions_whenSpecificTeamBelongsTo() {
        Competition competitionFaCup = Competition.builder().id(2L).name("FA Cup").teams(teams).build();
        competitions.add(competitionFaCup);
        chelseaTeam.addCompetition(competitionFaCup);
        arsenalTeam.addCompetition(competitionFaCup);

        when(competitionRepository.findCompetitionsByTeams(ArgumentMatchers.any())).thenReturn(new ArrayList<>(competitions));

        Set<Competition> competitionsFromDb = competitionServiceImpl.findCompetitionsByTeam(chelseaTeam);

        assertThat(competitionsFromDb, hasSize(2));
    }

    @Test
    void shouldReturnSavedCompetition_whenCompetitionIsSaved() {
        Competition competitionToBeSaved = Competition.builder().id(3L).name("Champions League").teams(teams).build();

        when(competitionRepository.save(ArgumentMatchers.any())).thenReturn(competitionToBeSaved);

        Competition savedCompetition = competitionServiceImpl.save(competitionToBeSaved);
        
        assertThat(savedCompetition.getId(), is(3L));
    }

    @Test
    void shouldDeleteCompetition_whenCompetitionWithSpecificIdExists() {
        competitionServiceImpl.deleteById(anyLong());
        verify(competitionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteCompetition_whenCompetitionWithSpecificNameExists() {
        competitionServiceImpl.delete(any());
        verify(competitionRepository,times(1)).delete(any());
    }
}