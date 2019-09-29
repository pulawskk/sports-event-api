package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.repository.ResultFootballRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ResultFootballServiceTest {

    @Mock
    private ResultFootballRepository resultFootballRepository;

    private ResultFootballService resultFootballService;

    private Team chelsea;
    private Team arsenal;
    private Competition premierLeague;
    private Set<Team> teams;
    private Game chelseaVsArsenal;
    private Odd chelseaOdd;
    private Odd arsenalOdd;
    private Odd drawOdd;
    private Set<Odd> odds;

    private GameReportFootball gameReportFootball;

    private ResultFootball resultFootball;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        resultFootballService = new ResultFootballService(resultFootballRepository);

        chelsea = Team.builder().id(1L).name("Chelsea").build();
        arsenal = Team.builder().id(2L).name("Arsenal").build();

        premierLeague = Competition.builder().id(1L).name("Premier League").build();

        teams = new HashSet<>();
        chelsea.addCompetition(premierLeague);
        arsenal.addCompetition(premierLeague);
        teams.add(chelsea);
        teams.add(arsenal);
        premierLeague.setTeams(teams);

        chelseaVsArsenal = Game.builder().id(1L).competition(premierLeague).teamAway(arsenal).teamHome(chelsea).startDate(Calendar.getInstance()).endDate(Calendar.getInstance()).build();

        chelseaOdd = Odd.builder().id(1L).type(GameOddType.HOME_WIN).game(chelseaVsArsenal).value(new BigDecimal("1.5")).build();
        arsenalOdd = Odd.builder().id(2L).type(GameOddType.AWAY_WIN).game(chelseaVsArsenal).value(new BigDecimal("3.0")).build();
        drawOdd = Odd.builder().id(3L).type(GameOddType.DRAW).game(chelseaVsArsenal).value(new BigDecimal("4.5")).build();

        odds = new HashSet<>();
        odds.add(chelseaOdd);
        odds.add(drawOdd);
        odds.add(arsenalOdd);

        chelseaVsArsenal.setOdds(odds);

        gameReportFootball = GameReportFootball.builder()
                .id(1L)
                .cornerAway(1)
                .cornerHome(2)
                .goalHome(3)
                .goalAway(4)
                .rCardAway(5)
                .rCardHome(6)
                .yCardAway(7)
                .yCardHome(8)
                .offsideAway(9)
                .offsideHome(10).build();

        resultFootball = ResultFootball.builder().id(1L).game(chelseaVsArsenal).gameReportFootball(gameReportFootball).build();
    }

    @Test
    void shouldReturnResultFootball_whenResultFootballWithSpecificIdExists() {
        when(resultFootballRepository.findById(anyLong())).thenReturn(Optional.of(resultFootball));

        ResultFootball resultFootballFromDb = resultFootballService.findById(1L);

        assertThat(resultFootballFromDb.getId(), is(1L));
        assertThat(resultFootballFromDb.getGame(), is(chelseaVsArsenal));
    }

    //TODO implement own queries to get Result by passing team or competition
//    @Test
//    void shouldReturnSetOfResultFootball_whenResultsFootballExistsForSpecificTeam() {
//
//    }
//
//    @Test
//    void shouldReturnSetOfResultFootball_whenResultsFootballExistsForSpecificCompetition() {
//
//    }

    @Test
    void shouldReturnSavedResultFootball_whenResultFootballIsSaved() {
        when(resultFootballRepository.save(any())).thenReturn(resultFootball);

        ResultFootball savedResultFootball = resultFootballService.save(resultFootball);

        assertThat(savedResultFootball.getId(), is(1L));
        assertThat(savedResultFootball.getGame(), is(chelseaVsArsenal));
    }

    @Test
    void shouldDeleteResultFootball_whenResultFootballExists() {
        resultFootballService.delete(resultFootball);
        verify(resultFootballRepository, times(1)).delete(any());
    }

    @Test
    void shouldDeleteResultFootball_whenResultFootballWithSpecificIdExists() {
        resultFootballService.deleteById(1L);
        verify(resultFootballRepository, times(1)).deleteById(anyLong());
    }
}
