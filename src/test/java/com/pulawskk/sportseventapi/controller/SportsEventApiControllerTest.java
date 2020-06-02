package com.pulawskk.sportseventapi.controller;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.CompetitionType;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.impl.GameReportFootballFootballService;
import com.pulawskk.sportseventapi.service.impl.GameServiceImpl;
import com.pulawskk.sportseventapi.service.impl.ResultFootballService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
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
    private List<ResultFootball> results;
    private List<Game> inplayGames;

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
        competition = Competition.builder().id(1L).name("FA CUP").type(CompetitionType.TOURNAMENT_ROUNDS).teams(teams).build();
        Set<Competition> competitions = new HashSet<>();
        competitions.add(competition);
        chelsea.setCompetitions(competitions);
        arsenal.setCompetitions(competitions);
        everton.setCompetitions(competitions);
        norwich.setCompetitions(competitions);

        gamesWithoutOdds = new HashSet<>();
        gamesWithOdds = new HashSet<>();

        Game firstGame = Game.builder().id(1L).teamHome(chelsea).teamAway(arsenal)
                .competition(competition)
                .status(GameStatus.PREMATCH)
                .build();
        Game secondGame = Game.builder().id(2L).teamHome(everton).teamAway(norwich)
                .competition(competition)
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

        inplayGames = Lists.newArrayList(gamesWithOdds);

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
        results = Lists.newArrayList(resultFirstGame, resultSecondGame);
    }

    @Test
    void shouldReturnJsonWithGames_whenPrematchGamesForExistCompetition() throws Exception {
        doReturn(inplayGames).when(gameService).generateInplayGamesForCompetition(anyLong());

        mockMvc.perform(get("/api/events/1/games")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['id']", is(inplayGames.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0]['uniqueId']", is(inplayGames.get(0).getUniqueId())))
                .andExpect(jsonPath("$[0]['status']", is(inplayGames.get(0).getStatus().toString())))
                .andExpect(jsonPath("$[0]['startDate']", is(inplayGames.get(0).getStartDate())))
                .andExpect(jsonPath("$[0]['endDate']", is(inplayGames.get(0).getEndDate())))
                .andExpect(jsonPath("$[0]['resultFootball']", isEmptyOrNullString()))
                .andExpect(jsonPath("$[0]['teamHome']['name']", is(inplayGames.get(0).getTeamHome().getName())))
                .andExpect(jsonPath("$[0]['teamAway']['name']", is(inplayGames.get(0).getTeamAway().getName())))
                .andExpect(jsonPath("$[0]['competition']['name']", is(inplayGames.get(0).getCompetition().getName())))
                .andExpect(jsonPath("$[0]['competition']['type']", is(inplayGames.get(0).getCompetition().getType().toString())));

        verify(gameService, times(1)).generateInplayGamesForCompetition(anyLong());
    }

    @Test
    void shouldReturnJsonWithResults_whenEnterApiEventsResults() throws Exception {
        doReturn(results).when(resultFootballService).generateAllResults();

        mockMvc.perform(get("/api/events/results")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['id']", is(results.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0]['gameReport']['id']", is(results.get(0).getGameReport().getId().intValue())))
                .andExpect(jsonPath("$[0]['gameReport']['goalHome']", is(results.get(0).getGameReport().getGoalHome())))
                .andExpect(jsonPath("$[0]['gameReport']['goalAway']", is(results.get(0).getGameReport().getGoalAway())))
                .andExpect(jsonPath("$[0]['gameReport']['offsideHome']", is(results.get(0).getGameReport().getOffsideHome())))
                .andExpect(jsonPath("$[0]['gameReport']['offsideAway']", is(results.get(0).getGameReport().getOffsideAway())))
                .andExpect(jsonPath("$[0]['gameReport']['cornerHome']", is(results.get(0).getGameReport().getCornerHome())))
                .andExpect(jsonPath("$[0]['gameReport']['cornerAway']", is(results.get(0).getGameReport().getCornerAway())))
                .andExpect(jsonPath("$[0]['gameReport']['ycardHome']", is(results.get(0).getGameReport().getYCardHome())))
                .andExpect(jsonPath("$[0]['gameReport']['ycardAway']", is(results.get(0).getGameReport().getYCardAway())))
                .andExpect(jsonPath("$[0]['gameReport']['rcardHome']", is(results.get(0).getGameReport().getRCardHome())))
                .andExpect(jsonPath("$[0]['gameReport']['rcardAway']", is(results.get(0).getGameReport().getRCardAway())));

        verify(resultFootballService, times(1)).generateAllResults();
    }

    @Test
    void shouldReturnJsonWithResults_whenEnterApiEventsResultsForCompetition() throws Exception {
        doReturn(results).when(resultFootballService).generateResultsForCompetition(anyLong());

        mockMvc.perform(get("/api/events/1/results")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]['id']", is(results.get(0).getId().intValue())))
                .andExpect(jsonPath("$[0]['gameReport']['id']", is(results.get(0).getGameReport().getId().intValue())))
                .andExpect(jsonPath("$[0]['gameReport']['goalHome']", is(results.get(0).getGameReport().getGoalHome())))
                .andExpect(jsonPath("$[0]['gameReport']['goalAway']", is(results.get(0).getGameReport().getGoalAway())))
                .andExpect(jsonPath("$[0]['gameReport']['offsideHome']", is(results.get(0).getGameReport().getOffsideHome())))
                .andExpect(jsonPath("$[0]['gameReport']['offsideAway']", is(results.get(0).getGameReport().getOffsideAway())))
                .andExpect(jsonPath("$[0]['gameReport']['cornerHome']", is(results.get(0).getGameReport().getCornerHome())))
                .andExpect(jsonPath("$[0]['gameReport']['cornerAway']", is(results.get(0).getGameReport().getCornerAway())))
                .andExpect(jsonPath("$[0]['gameReport']['ycardHome']", is(results.get(0).getGameReport().getYCardHome())))
                .andExpect(jsonPath("$[0]['gameReport']['ycardAway']", is(results.get(0).getGameReport().getYCardAway())))
                .andExpect(jsonPath("$[0]['gameReport']['rcardHome']", is(results.get(0).getGameReport().getRCardHome())))
                .andExpect(jsonPath("$[0]['gameReport']['rcardAway']", is(results.get(0).getGameReport().getRCardAway())));

        verify(resultFootballService, times(1)).generateResultsForCompetition(anyLong());
    }
}