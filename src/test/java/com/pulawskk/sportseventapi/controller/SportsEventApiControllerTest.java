package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SportsEventApiControllerTest {

    private SportsEventApiController sportsEventApiController;

    @Mock
    private GameServiceImpl gameService;

    @Mock
    private GameReportFootballFootballService gameReportFootballFootballService;

    @Mock
    private ResultFootballService resultFootballService;

    private MockMvc mockMvc;

    private Competition competition;
    private Set<Team> teams;
    private Set<Game> gamesWithoutOdds;
    private Set<Game> gamesWithOdds;
    private Set<ResultFootball> results;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        sportsEventApiController = new SportsEventApiController(gameService, gameReportFootballFootballService, resultFootballService);

        mockMvc =  MockMvcBuilders.standaloneSetup(sportsEventApiController)
                .build();

        Team chelsea = Team.builder().id(1L).name("Chelsea").build();
        Team arsenal = Team.builder().id(2L).name("Arsenal").build();
        Team everton = Team.builder().id(3L).name("Everton").build();
        Team norwich = Team.builder().id(4L).name("Norwich").build();
        teams = new HashSet<>();
        teams.add(chelsea);
        teams.add(arsenal);
        teams.add(everton);
        teams.add(norwich);
        competition = Competition.builder().id(1L).name("FA CUP").teams(teams).build();
        Set<Competition> competitions = new HashSet<>();
        competitions.add(competition);
        chelsea.setCompetitions(competitions);
        arsenal.setCompetitions(competitions);
        everton.setCompetitions(competitions);
        norwich.setCompetitions(competitions);

        gamesWithoutOdds = new HashSet<>();
        gamesWithOdds = new HashSet<>();

        Game firstGame = Game.builder().id(1L).teamHome(chelsea).teamAway(arsenal)
                .status(GameStatus.PREMATCH)
                .build();
        Game secondGame = Game.builder().id(2L).teamHome(everton).teamAway(norwich)
                .status(GameStatus.PREMATCH)
                .build();
        gamesWithoutOdds.add(firstGame);
        gamesWithoutOdds.add(secondGame);

        Odd oddHomeFirst = Odd.builder().id(1L).type(GameOddType.HOME_WIN).value(new BigDecimal("1.4")).build();
        Odd oddDrawFirst = Odd.builder().id(2L).type(GameOddType.DRAW).value(new BigDecimal("3.4")).build();
        Odd oddAwayFirst = Odd.builder().id(3L).type(GameOddType.AWAY_WIN).value(new BigDecimal("6.4")).build();

        Odd oddHomeSecond = Odd.builder().id(1L).type(GameOddType.HOME_WIN).value(new BigDecimal("1.7")).build();
        Odd oddDrawSecond = Odd.builder().id(2L).type(GameOddType.DRAW).value(new BigDecimal("2.4")).build();
        Odd oddAwaySecond = Odd.builder().id(3L).type(GameOddType.AWAY_WIN).value(new BigDecimal("5.0")).build();

        Set<Odd> oddsFirstGame = new HashSet<>();
        Set<Odd> oddsSecondGame = new HashSet<>();

        oddsFirstGame.add(oddHomeFirst);
        oddsFirstGame.add(oddDrawFirst);
        oddsFirstGame.add(oddAwayFirst);

        oddsSecondGame.add(oddHomeSecond);
        oddsSecondGame.add(oddDrawSecond);
        oddsSecondGame.add(oddAwaySecond);

        firstGame.setOdds(oddsFirstGame);
        secondGame.setOdds(oddsSecondGame);

        gamesWithOdds.add(firstGame);
        gamesWithOdds.add(secondGame);

        results = new HashSet<>();

        GameReportFootball gameFirstReportFootball = GameReportFootball.builder().id(1L)
                .cornerAway(1).cornerHome(2)
                .goalHome(3).goalAway(4)
                .rCardAway(5).rCardHome(6)
                .yCardAway(7).yCardHome(8)
                .offsideAway(9).offsideHome(10).build();

        GameReportFootball gameSecondReportFootball = GameReportFootball.builder().id(2L)
                .cornerAway(1).cornerHome(2)
                .goalHome(3).goalAway(4)
                .rCardAway(5).rCardHome(6)
                .yCardAway(7).yCardHome(8)
                .offsideAway(9).offsideHome(10).build();

        ResultFootball resultFirstGame = ResultFootball.builder().id(1L).game(firstGame).gameReportFootball(gameFirstReportFootball).build();
        ResultFootball resultSecondGame = ResultFootball.builder().id(2L).game(secondGame).gameReportFootball(gameSecondReportFootball).build();
        results.add(resultFirstGame);
        results.add(resultSecondGame);
    }

    @Test
    void shouldReturnJsonWithGames_whenEnterApiEventsGames() throws Exception {
        List<JSONObject> jsonGames = new ArrayList<>();

        gamesWithOdds.forEach(game -> {
            jsonGames.add(gameService.generateJsonFromGame(game));
        });
        when(gameService.generateJsonForInplayGames(anyLong())).thenReturn(jsonGames);

        mockMvc.perform(get("/api/events/1/games")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=ISO-8859-1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnJsonWithResults_whenEnterApiEventsResults() throws Exception {
        when(resultFootballService.findAllResultsForCompetition(any())).thenReturn(results);

        mockMvc.perform(get("/api/events/results")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=ISO-8859-1"))
                .andExpect(status().isOk());
    }
}