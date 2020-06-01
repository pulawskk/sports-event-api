package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.*;
import com.pulawskk.sportseventapi.enums.CompetitionType;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    @Mock
    private CompetitionService competitionService;

    @Mock
    private HttpPostService httpPostService;

    @Mock
    private JmsService jmsService;

    private Competition competition;
    private Set<Team> teams;

    private Team chelseaTeam;
    private Team arsenalTeam;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        fakeFootballService = new FakeFootballService(teamService, gameService, oddService, gameReportFootballService, resultFootballService, competitionService, jmsService, httpPostService);
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
                                .map(Team::new)
                                .collect(Collectors.toSet());
        competition.setTeams(teams);
        teams.forEach(team -> team.setCompetitions(competitions));

        chelseaTeam = Team.builder().id(31L).name("Chelsea")
                .competitions(competitions).build();

        arsenalTeam = Team.builder().id(32L).name("Arsenal")
                .competitions(competitions).build();
    }

    @Test
    void shouldReturnSetOfGamesTwiceLess_whenCompetitionHasEvenNumberOfTeams() {
        when(teamService.findAllByCompetitions(competition)).thenReturn(teams);
        Game gameForSaved = Game.builder().id(1L).build();

        when(gameService.save(any())).thenReturn(gameForSaved);
        when(gameService.saveAll(anySet())).thenReturn(anySet());

        Set<Game> games = fakeFootballService.generateGames(competition);

        assertAll(() -> {
            assertThat(games, hasSize(competition.getTeams().size()/2));
            assertThat(games.iterator().next().getResultFootball(), is(nullValue()));
            assertThat(games.iterator().next().getStatus(), is(GameStatus.CREATED));
            assertThat(games.iterator().next().getCompetition(), is(competition));
        });
    }

    @Test
    void shouldReturnSetOfGamesWithUpdatedOdds_whenSetOfGamesIsGiven() {
        when(teamService.findAllByCompetitions(competition)).thenReturn(teams);
        Game gameForSave = Game.builder().id(1L)
                .teamHome(chelseaTeam).teamAway(arsenalTeam)
                .competition(competition).build();
        when(gameService.save(any())).thenReturn(gameForSave);

        Odd oddForSave = Odd.builder().id(1L).build();
        when(oddService.save(any())).thenReturn(oddForSave);

//        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competition);
        Game gameWithOdds = fakeFootballService.generateOdds(gameForSave);

        assertAll(() -> {
            assertThat(gameWithOdds.getStatus(), is(GameStatus.PREMATCH));
            assertThat(gameWithOdds.getOdds(), hasSize(3));
            assertThat(gameWithOdds.getTeamHome(), is(notNullValue()));
            assertThat(gameWithOdds.getTeamAway(), is(notNullValue()));
            assertThat(gameWithOdds.getResultFootball(), is(nullValue()));
            assertThat(gameWithOdds.getCompetition().getName(), is(competition.getName()));
            assertThat(gameWithOdds.getCompetition().getId(), is(1L));
        });
    }

    @Test
    void shouldReturnSetOfResult_whenSetOfGamesWithOddsIsGivenAndCompetitionIsLeague() {
        competition.setType(CompetitionType.LEAGUE);
        when(teamService.findAllByCompetitions(competition)).thenReturn(teams);

        Game gameForSave = Game.builder().id(1L).teamHome(chelseaTeam).teamAway(arsenalTeam)
                .competition(competition).build();
        Set<Game> gamesForSave = new HashSet<>();
        gamesForSave.add(gameForSave);
        when(gameService.save(any())).thenReturn(gameForSave);
        when(gameService.saveAll(anySet())).thenReturn(gamesForSave);

        Odd oddForSave = Odd.builder().id(1L).build();
        when(oddService.save(any())).thenReturn(oddForSave);

        ResultFootball resultFootballForSaved = ResultFootball.builder().id(1L).build();
        Set<ResultFootball> resultsFootballForSaved = new HashSet<>();
        resultsFootballForSaved.add(resultFootballForSaved);
        when(resultFootballService.saveAll(anySet())).thenReturn(resultsFootballForSaved);

        GameReportFootball gameReportFootballForSaved = GameReportFootball.builder().id(1L).build();
        when(gameReportFootballService.save(any())).thenReturn(gameReportFootballForSaved);

        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competition);
        Game gameWithOdds = fakeFootballService.generateOdds(gameForSave);
        Set<ResultFootball> result = fakeFootballService.generateResults(Sets.newSet(gameWithOdds));

        assertAll(() -> {
            assertThat(result, hasSize(1));
            assertThat(result.iterator().next().getGameReport().getGoalHome(), greaterThan(-1));
            assertThat(result.iterator().next().getGameReport().getCornerAway(), greaterThan(-1));
        });

        verify(resultFootballService, times(1)).saveAll(anySet());
        verify(competitionService, never()).save(any());
        verify(teamService, never()).save(any());
        verify(teamService, never()).findByName(anyString());
    }

    @Test
    void shouldReturnSetOfResult_whenSetOfGamesWithOddsIsGivenAndCompetitionIsTournamentRounds() {
        Competition competitionTournamentRounds = Competition.builder().id(4L).type(CompetitionType.TOURNAMENT_ROUNDS).name("FA Cup").build();
        Set<Team> teams = new HashSet<>();
        teams.add(chelseaTeam);
        teams.add(arsenalTeam);
        competitionTournamentRounds.setTeams(teams);
        Set<Competition> competitionTournamentRoundsSet = new HashSet<>();
        competitionTournamentRoundsSet.add(competitionTournamentRounds);
        teams.forEach(t -> t.setCompetitions(competitionTournamentRoundsSet));
        when(teamService.findAllByCompetitions(competitionTournamentRounds)).thenReturn(teams);

        Game gameForSave = Game.builder().id(1L).teamHome(chelseaTeam).teamAway(arsenalTeam)
                .competition(competition).build();
        Set<Game> gamesForSave = new HashSet<>();
        gamesForSave.add(gameForSave);
        when(gameService.save(any())).thenReturn(gameForSave);
        when(gameService.saveAll(anySet())).thenReturn(gamesForSave);

        Odd oddForSave = Odd.builder().id(1L).build();
        when(oddService.save(any())).thenReturn(oddForSave);

        ResultFootball resultFootballForSaved = ResultFootball.builder().id(1L).build();
        Set<ResultFootball> resultsFootballForSaved = new HashSet<>();
        resultsFootballForSaved.add(resultFootballForSaved);
        when(resultFootballService.saveAll(anySet())).thenReturn(resultsFootballForSaved);

        GameReportFootball gameReportFootballForSaved = GameReportFootball.builder().id(1L).build();
        when(gameReportFootballService.save(any())).thenReturn(gameReportFootballForSaved);

        doReturn(Team.builder().competitions(competitionTournamentRoundsSet).name("Chelsea").build()).when(teamService).findByName(anyString());

        Set<Game> gamesWithoutOdds = fakeFootballService.generateGames(competitionTournamentRounds);
        Game gameWithOdds = fakeFootballService.generateOdds(gameForSave);
        Set<ResultFootball> result = fakeFootballService.generateResults(Sets.newSet(gameWithOdds));

        assertAll(() -> {
            assertThat(result, hasSize(1));
            assertThat(result.iterator().next().getGameReport().getGoalHome(), greaterThan(-1));
            assertThat(result.iterator().next().getGameReport().getCornerAway(), greaterThan(-1));
        });

        verify(resultFootballService, times(1)).saveAll(anySet());
        verify(teamService, times(1)).findAllByCompetitions(any());
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
                                  .teamHome(chelseaTeam)
                                  .teamAway(arsenalTeam)
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

    @Test
    void shouldGenerateGamesForFaCup_whenGamesWithOddsAreAvailable() {
        //given
        Team chelseaTeam = Team.builder().id(1L).name("Chelsea").build();
        Team westHamTeam = Team.builder().id(2L).name("West Ham").build();
        Team arsenalTeam = Team.builder().id(3L).name("Arsenal").build();
        Team crystalPalaceTeam = Team.builder().id(4L).name("Crystal Palace").build();
        Set<Team> teamSet = Sets.newSet(chelseaTeam, westHamTeam, arsenalTeam, crystalPalaceTeam);

        Competition faCupTournament = Competition.builder()
                .id(11L).type(CompetitionType.TOURNAMENT_ROUNDS)
                .name("FA Cup").teams(teamSet).build();
        teamSet.forEach(t -> t.setCompetitions(Sets.newSet(faCupTournament)));

        Game chelseaVsWestHamGame = Game.builder().id(21L).competition(faCupTournament).teamHome(chelseaTeam).teamAway(westHamTeam).status(GameStatus.PREMATCH).build();
        Game arsenalVsCrystalPalaceGame = Game.builder().id(22L).competition(faCupTournament).teamHome(arsenalTeam).teamAway(crystalPalaceTeam).status(GameStatus.PREMATCH).build();
        Set<Game> gameSet = Sets.newSet(chelseaVsWestHamGame, arsenalVsCrystalPalaceGame);

        doReturn(faCupTournament).when(competitionService).findByName("FA Cup");
        doReturn(teamSet).when(teamService).findAllByCompetitions(faCupTournament);
        doReturn(chelseaVsWestHamGame).when(gameService).save(any());
        doReturn(gameSet).when(gameService).saveAll(gameSet);
        doReturn(Odd.builder().build()).when(oddService).save(any());
        doNothing().when(jmsService).sendJsonMessage(anyString(), any());

        //when
        fakeFootballService.generateGamesForFaCup();

        //then
        verify(oddService, times(3 * gameSet.size())).save(any());
        verify(gameService, times(1)).saveAll(anySet());
    }

    @Test
    void shouldGenerateGamesForPremierLeague_whenGamesWithOddsAreAvailable() throws IOException {
        //given
        Team chelseaTeam = Team.builder().id(1L).name("Chelsea").build();
        Team westHamTeam = Team.builder().id(2L).name("West Ham").build();
        Team arsenalTeam = Team.builder().id(3L).name("Arsenal").build();
        Team crystalPalaceTeam = Team.builder().id(4L).name("Crystal Palace").build();
        Set<Team> teamSet = Sets.newSet(chelseaTeam, westHamTeam, arsenalTeam, crystalPalaceTeam);

        Competition premierLeagueTournament = Competition.builder()
                .id(11L).type(CompetitionType.LEAGUE)
                .name("Premier League").teams(teamSet).build();
        teamSet.forEach(t -> t.setCompetitions(Sets.newSet(premierLeagueTournament)));

        Game chelseaVsWestHamGame = Game.builder().id(21L).competition(premierLeagueTournament).teamHome(chelseaTeam).teamAway(westHamTeam).status(GameStatus.PREMATCH).build();
        Game arsenalVsCrystalPalaceGame = Game.builder().id(22L).competition(premierLeagueTournament).teamHome(arsenalTeam).teamAway(crystalPalaceTeam).status(GameStatus.PREMATCH).build();
        Set<Game> gameSet = Sets.newSet(chelseaVsWestHamGame, arsenalVsCrystalPalaceGame);

        doReturn(premierLeagueTournament).when(competitionService).findByName("Premier League");
        doReturn(teamSet).when(teamService).findAllByCompetitions(premierLeagueTournament);
        doReturn(chelseaVsWestHamGame).when(gameService).save(any());
        doReturn(gameSet).when(gameService).saveAll(gameSet);
        doReturn(Odd.builder().build()).when(oddService).save(any());
        doNothing().when(httpPostService).postJsonMessage(any(), anyString());
        fakeFootballService.setBettingServerIp("ip");
        fakeFootballService.setBettingServerPort("port");

        //when
        fakeFootballService.generateGamesForPremierLeague();

        //then
        verify(oddService, times(3 * gameSet.size())).save(any());
        verify(gameService, times(1)).saveAll(anySet());
    }

    @DisplayName("check bettingsite server params : ")
    @ParameterizedTest(name = "[{index}] {displayName} ip = [{0}] & port = [{1}] should invoke postJonMessage {2} times")
    @MethodSource("getBettingServerVariables")
    void shouldNotGenerateGameForPremierLeague_whenBettingServerPortOrIpIsBlank(
            String serverIp, String serverPort, int timesOfPostJsonMessageInvocation) throws IOException {

        //given
        Team chelseaTeam = Team.builder().id(1L).name("Chelsea").build();
        Team westHamTeam = Team.builder().id(2L).name("West Ham").build();
        Team arsenalTeam = Team.builder().id(3L).name("Arsenal").build();
        Team crystalPalaceTeam = Team.builder().id(4L).name("Crystal Palace").build();
        Set<Team> teamSet = Sets.newSet(chelseaTeam, westHamTeam, arsenalTeam, crystalPalaceTeam);

        Competition premierLeagueTournament = Competition.builder()
                .id(11L).type(CompetitionType.LEAGUE)
                .name("Premier League").teams(teamSet).build();
        teamSet.forEach(t -> t.setCompetitions(Sets.newSet(premierLeagueTournament)));

        Game chelseaVsWestHamGame = Game.builder().id(21L).competition(premierLeagueTournament).teamHome(chelseaTeam).teamAway(westHamTeam).status(GameStatus.PREMATCH).build();
        Game arsenalVsCrystalPalaceGame = Game.builder().id(22L).competition(premierLeagueTournament).teamHome(arsenalTeam).teamAway(crystalPalaceTeam).status(GameStatus.PREMATCH).build();
        Set<Game> gameSet = Sets.newSet(chelseaVsWestHamGame, arsenalVsCrystalPalaceGame);

        doReturn(premierLeagueTournament).when(competitionService).findByName("Premier League");
        doReturn(teamSet).when(teamService).findAllByCompetitions(premierLeagueTournament);
        doReturn(chelseaVsWestHamGame).when(gameService).save(any());
        doReturn(gameSet).when(gameService).saveAll(gameSet);
        doReturn(Odd.builder().build()).when(oddService).save(any());
        doNothing().when(httpPostService).postJsonMessage(any(), anyString());

        fakeFootballService.setBettingServerPort(serverPort);
        fakeFootballService.setBettingServerIp(serverIp);

        //when
        fakeFootballService.generateGamesForPremierLeague();

        //then
        verify(httpPostService, times(timesOfPostJsonMessageInvocation)).postJsonMessage(any(), anyString());
    }

    private static Stream<Arguments> getBettingServerVariables() {
        return Stream.of(
                Arguments.of("", "", 0),
                Arguments.of(null, "", 0),
                Arguments.of("", null, 0),
                Arguments.of(null, null, 0),
                Arguments.of("ip", "", 0),
                Arguments.of("ip", null, 0),
                Arguments.of(null, "port", 0),
                Arguments.of("", "port", 0),
                Arguments.of("ip", "port", 2)
        );
    }

    @Test
    void shouldNotGenerateGamesForFaCup_whenGamesWithOddsAreNotAvailable() {
        //given
        competition.setTeams(new HashSet<>());
        doReturn(competition).when(competitionService).findByName(anyString());

        //when
        fakeFootballService.generateGamesForFaCup();

        //then
        verify(competitionService, times(1)).findByName(anyString());
        verify(jmsService, never()).sendJsonMessage(anyString(), any());
    }

    @Test
    void shouldNotGenerateGamesForPremierLeague_whenGamesWithOddsAreNotAvailable() throws IOException {
        //given
        competition.setTeams(new HashSet<>());
        doReturn(competition).when(competitionService).findByName(anyString());

        //when
        fakeFootballService.generateGamesForFaCup();

        //then
        verify(competitionService, times(1)).findByName(anyString());
        verify(httpPostService, never()).postJsonMessage(any(), anyString());
    }
}