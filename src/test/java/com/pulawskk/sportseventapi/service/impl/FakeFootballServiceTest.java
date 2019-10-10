package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.GameReportFootballService;
import com.pulawskk.sportseventapi.service.GameService;
import com.pulawskk.sportseventapi.service.OddService;
import com.pulawskk.sportseventapi.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.*;

class FakeFootballServiceTest {

    private FakeFootballService fakeFootballService;

    @Mock
    private TeamService teamService;

    @Mock
    private GameService gameService;

    @Mock
    private OddService oddService;

    @Mock
    private GameReportFootballService gameReportFootballService;

    @Mock
    private ResultFootballService resultFootballService;

    private Competition competition;
    private Set<Team> teams;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fakeFootballService = new FakeFootballService(teamService, gameService, oddService, gameReportFootballService, resultFootballService);
        competition = Competition.builder().id(1L).name("Premier League").build();
        Set<Competition> competitions = new HashSet<>();
        competitions.add(competition);
        teams = Stream.of("Chelsea", "Arsenal",
                                    "Manchester United", "Manchester City",
                                    "Newcastle United", "West Ham United",
                                    "Crystal Palace", "Aston Villa",
                                    "Norwich", "Leicester",
                                    "Southampton", "Sheffield United",
                                    "Tottenham", "Brighton",
                                    "Everton", "Liverpool",
                                    "Watford", "Wolverhampton Wanders",
                                    "Bournemouth", "Burnley")
                                .map((teamName) -> new Team(teamName))
                                .collect(Collectors.toSet());
        competition.setTeams(teams);
        teams.forEach(team -> team.setCompetitions(competitions));
    }

    @Test
    void shouldReturnSetOfGamesTwiceLess_whenCompetitionHasEvenNumberOfTeams() {
        when(teamService.findAllByCompetitions(competition)).thenReturn(teams);
        Game gameForSave = Game.builder().id(1L).build();
        when(gameService.save(any())).thenReturn(gameForSave);

        Set<Game> games = fakeFootballService.generateGames(competition);

        assertAll(() -> {
            assertThat(games, hasSize(competition.getTeams().size()/2));
            assertThat(games.iterator().next().getResultFootball(), is(nullValue()));
            assertThat(games.iterator().next().getStatus(), is(GameStatus.PREMATCH));
            assertThat(games.iterator().next().getCompetition(), is(competition));
            assertThat(games.iterator().next().getId(), is(1L));
            assertThat(games.containsAll(games), notNullValue());
        });
    }

    @Test
    void shouldReturnSetOfGamesWithUpdatedOdds_whenSetOfGamesIsGiven() {
        when(teamService.findAllByCompetitions(competition)).thenReturn(teams);
        Game gameForSave = Game.builder().id(1L).build();
        when(gameService.save(any())).thenReturn(gameForSave);

        Odd oddForSave = Odd.builder().id(1L).build();
        when(oddService.save(any())).thenReturn(oddForSave);

        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competition);
        Set<Game> gamesWithOdds = fakeFootballService.generateOdds(gamesWithoutOdds);

        gamesWithOdds.forEach(game -> {
            assertAll(() -> {
                assertThat(game.getStatus(), is(GameStatus.PREMATCH));
                assertThat(game.getOdds(), hasSize(3));
                assertThat(game.getTeamHome(), is(notNullValue()));
                assertThat(game.getTeamAway(), is(notNullValue()));
                assertThat(game.getResultFootball(), is(nullValue()));
                assertThat(game.getCompetition().getName(), is(competition.getName()));
                assertThat(game.getCompetition().getId(), is(1L));
            });
        });
    }

    @Test
    void shouldReturnSetOfResult_whenSetOfGamesWithOddsIsGiven() {
        when(teamService.findAllByCompetitions(competition)).thenReturn(teams);
        Game gameForSave = Game.builder().id(1L).build();
        when(gameService.save(any())).thenReturn(gameForSave);

        Odd oddForSave = Odd.builder().id(1L).build();
        when(oddService.save(any())).thenReturn(oddForSave);

        ResultFootball resultFootballForSaved = ResultFootball.builder().id(1L).build();
        when(resultFootballService.save(any())).thenReturn(resultFootballForSaved);

        GameReportFootball gameReportFootballForSaved = GameReportFootball.builder().id(1L).build();
        when(gameReportFootballService.save(any())).thenReturn(gameReportFootballForSaved);

        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competition);
        Set<Game> gamesWithOdds = fakeFootballService.generateOdds(gamesWithoutOdds);
        Set<ResultFootball> result = fakeFootballService.generateResult(gamesWithOdds);

        assertAll(() -> {
            assertThat(result.iterator().next().getGameReport().getGoalHome(), greaterThan(-1));
            assertThat(result.iterator().next().getGameReport().getCornerAway(), greaterThan(-1));
            assertThat(result.iterator().next().getGame().getStatus(), is(GameStatus.RESULTED));
            assertThat(result.iterator().next().getGame().getId(), is(1L));
            assertThat(result.iterator().next().getGame().getTeamAway(), is(notNullValue()));
        });
    }

    @Test
    void shouldReturnGameReportFootball_whenGameIsGiven() {
        Set<Competition> competitions = new HashSet<>();
        Odd oddH = Odd.builder().type(GameOddType.HOME_WIN).value(new BigDecimal("1.5")).build();
        Odd oddX = Odd.builder().type(GameOddType.DRAW).value(new BigDecimal("3.5")).build();
        Odd oddA = Odd.builder().type(GameOddType.AWAY_WIN).value(new BigDecimal("5.5")).build();

        Set<Odd> odds = new HashSet<>();
        odds.add(oddH);
        odds.add(oddA);
        odds.add(oddX);

        competitions.add(competition);
        Game game = Game.builder().id(1L)
                                  .teamHome(Team.builder()
                                          .name("Chelsea")
                                          .competitions(competitions).build())
                                  .teamAway(Team.builder().name("Arsenal")
                                          .competitions(competitions).build())
                                  .odds(odds)
                                  .build();

        GameReportFootball gameReportFootballForSave = GameReportFootball.builder().id(1L).build();
        when(gameReportFootballService.save(any())).thenReturn(gameReportFootballForSave);

        GameReportFootball gameReportFootball = fakeFootballService.generateReportFootball(game);

        assertAll(() -> {
            assertThat(gameReportFootball.getCornerHome(), greaterThan(-1));
            assertThat(gameReportFootball.getCornerAway(), greaterThan(-1));
            assertThat(gameReportFootball.getYCardAway(), greaterThan(-1));
            assertThat(gameReportFootball.getYCardHome(), greaterThan(-1));
            assertThat(gameReportFootball.getRCardAway(), greaterThan(-1));
            assertThat(gameReportFootball.getRCardHome(), greaterThan(-1));
            assertThat(gameReportFootball.getGoalAway(), greaterThan(-1));
            assertThat(gameReportFootball.getGoalHome(), greaterThan(-1));
        });
    }

    @Test
    void shouldGenerateListOfIntegersWithSizeTwiceLessThanTeams_whenCompetitionHasTeams() {
        List<Integer> pairsOrder = fakeFootballService.generatePairs(competition);
        assertThat(pairsOrder, hasSize(competition.getTeams().size()));
    }

    @Test
    void shouldReturnIntFromOneToNine_whenGenerateRandomValueRangeOneToNineIsCalled() {
        for(int i =0; i < 100; i++) {
            int number = fakeFootballService.generateRandomValueRangeOneToNine();
            assertThat(number, lessThan(10));
            assertThat(number, greaterThan(0));
        }
    }
}