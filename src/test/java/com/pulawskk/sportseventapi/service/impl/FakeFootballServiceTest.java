package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.hamcrest.Matchers.*;

class FakeFootballServiceTest {

    private FakeFootballService fakeFootballService;

    private Competition competition;
    private Set<Team> teams;

    @BeforeEach
    void setUp() {
        fakeFootballService = new FakeFootballService();
        competition = Competition.builder().id(1L).name("Premier League").build();
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
                                .map(Team::new)
                                .collect(Collectors.toSet());
        competition.setTeams(teams);
    }

    @Test
    void shouldReturnSetOfGamesTwiceLess_whenCompetitionHasEvenNumberOfTeams() {
        Set<Game> games = fakeFootballService.generateGames(competition);

        assertAll(() -> {
            assertThat(games, hasSize(competition.getTeams().size()/2));
            assertThat(games.iterator().next().getResultFootball(), is(nullValue()));
            assertThat(games.iterator().next().getStatus(), is(GameStatus.PREMATCH));
            assertThat(games.containsAll(games), notNullValue());
        });
    }

    @Test
    void shouldReturnSetOfGamesWhichUpdatedOdds_whenSetOfGamesIsGiven() {
        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competition);
        Set<Game> gamesWithOdds = fakeFootballService.generateOdds(gamesWithoutOdds);

        gamesWithOdds.forEach(game -> {
            assertAll(() -> {
                assertThat(game.getStatus(), is(GameStatus.PREMATCH));
                assertThat(game.getOdds(), hasSize(3));
                assertThat(game.getTeamHome(), is(notNullValue()));
                assertThat(game.getTeamAway(), is(notNullValue()));
                assertThat(game.getResultFootball(), is(notNullValue()));
                assertThat(game.getCompetition().getName(), is(competition.getName()));
            });
        });
    }

    @Test
    void shouldReturnSetOfResult_whenSetOfGamesWithOddsIsGiven() {
        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competition);
        Set<Game> gamesWithOdds = fakeFootballService.generateOdds(gamesWithoutOdds);
        Set<ResultFootball> result = fakeFootballService.generateResult(gamesWithOdds);

        assertAll(() -> {
            assertThat(result.iterator().next().getGameReport().getGoalHome(), greaterThan(-1));
            assertThat(result.iterator().next().getGameReport().getCornerAway(), greaterThan(-1));
            assertThat(result.iterator().next().getGame().getStatus(), is(GameStatus.RESULTED));
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
}