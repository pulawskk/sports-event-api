package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Odd;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.enums.GameStatus;
import com.pulawskk.sportseventapi.repository.GameRepository;
import com.pulawskk.sportseventapi.repository.OddRepository;
import org.assertj.core.util.Lists;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GameServiceImplTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private OddRepository oddRepository;

    private GameServiceImpl gameServiceImpl;

    private Team chelsea;
    private Team arsenal;
    private Competition premierLeague;
    private Set<Team> teams;
    private Game chelseaVsArsenal;
    private Odd chelseaOdd;
    private Odd arsenalOdd;
    private Odd drawOdd;
    private Set<Odd> odds;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        gameServiceImpl = new GameServiceImpl(gameRepository, oddRepository);

        chelsea = Team.builder().id(1L).name("Chelsea").build();
        arsenal = Team.builder().id(2L).name("Arsenal").build();

        premierLeague = Competition.builder().id(1L).name("Premier League").build();

        teams = new HashSet<>();
        chelsea.addCompetition(premierLeague);
        arsenal.addCompetition(premierLeague);
        teams.add(chelsea);
        teams.add(arsenal);
        premierLeague.setTeams(teams);

        chelseaVsArsenal = Game.builder().id(1L).competition(premierLeague).teamAway(arsenal).teamHome(chelsea).startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).build();

        chelseaOdd = Odd.builder().id(1L).type(GameOddType.HOME_WIN).game(chelseaVsArsenal).value(new BigDecimal("1.5")).build();
        arsenalOdd = Odd.builder().id(2L).type(GameOddType.AWAY_WIN).game(chelseaVsArsenal).value(new BigDecimal("3.0")).build();
        drawOdd = Odd.builder().id(3L).type(GameOddType.DRAW).game(chelseaVsArsenal).value(new BigDecimal("4.5")).build();

        odds = new HashSet<>();
        odds.add(chelseaOdd);
        odds.add(drawOdd);
        odds.add(arsenalOdd);

        chelseaVsArsenal.setOdds(odds);
    }

    @Test
    void shouldReturnGame_whenGameWithSpecificIdExists() {
        when(gameRepository.findById(anyLong())).thenReturn(Optional.of(chelseaVsArsenal));

        Game gameFromDb = gameServiceImpl.findGameById(1L);

        assertAll(() -> {
            assertThat(gameFromDb.getId(), is(1L));
            assertThat(gameFromDb.getTeamHome(), is(chelsea));
        });
    }

    @Test
    void shouldReturnSetOfGames_whenGameWithSpecificTeamExists() {
        Team newcastle = Team.builder().id(3L).name("Newcastle").build();
        newcastle.addCompetition(premierLeague);

        Game newcastleVsChelsea = Game.builder().id(2L).teamHome(newcastle).teamAway(chelsea).competition(premierLeague).startDate(LocalDateTime.now()).endDate(LocalDateTime.now()).build();

        Odd newcastleOdd = Odd.builder().id(4L).type(GameOddType.HOME_WIN).value(new BigDecimal("5.0")).game(newcastleVsChelsea).build();
        Odd drawOdd = Odd.builder().id(5L).type(GameOddType.DRAW).value(new BigDecimal("3.2")).game(newcastleVsChelsea).build();
        Odd chelseaOdd = Odd.builder().id(6L).type(GameOddType.AWAY_WIN).value(new BigDecimal("1.7")).game(newcastleVsChelsea).build();
        Set<Odd> oddsForGame = new HashSet<>();
        oddsForGame.add(drawOdd);
        oddsForGame.add(chelseaOdd);
        oddsForGame.add(newcastleOdd);

        newcastleVsChelsea.setOdds(oddsForGame);

        Set<Game> games = new HashSet<>();
        games.add(chelseaVsArsenal);
        games.add(newcastleVsChelsea);

        when(gameRepository.findAllByTeamAwayOrTeamHome(any())).thenReturn(new ArrayList<>(games));

        Set<Game> gamesFromDb = new HashSet<>(gameServiceImpl.findAllByTeamAwayOrTeamHome(chelsea.getId()));

        assertAll(() -> {
            assertThat(gamesFromDb, hasSize(2));
            assertThat(gamesFromDb, hasItem(chelseaVsArsenal));
        });
    }

    @Test
    void shouldReturnSetOfGames_whenGameWithSpecificTeamHomeExists() {
        List<Game> specificTeam = new ArrayList<>();
        specificTeam.add(chelseaVsArsenal);
        when(gameRepository.findAllByTeamHome(any())).thenReturn(specificTeam);

        Set<Game> gamesFromDb = new HashSet<>(gameServiceImpl.findAllByTeamHome(chelsea));

        assertAll(() -> {
            assertThat(gamesFromDb, hasSize(1));
            assertThat(gamesFromDb, hasItem(chelseaVsArsenal));
        });
    }

    @Test
    void shouldReturnSetOfGames_whenGameWithSpecificTeamAwayExists() {
        List<Game> specificTeam = new ArrayList<>();
        specificTeam.add(chelseaVsArsenal);
        when(gameRepository.findAllByTeamAway(any())).thenReturn(specificTeam);

        Set<Game> gamesFromDb = new HashSet<>(gameServiceImpl.findAllByTeamAway(arsenal));

        assertAll(() -> {
            assertThat(gamesFromDb, hasSize(1));
            assertThat(gamesFromDb, hasItem(chelseaVsArsenal));
        });
    }

    @Test
    void shouldReturnSavedItem_whenGameIsSaved() {
        when(gameRepository.save(any())).thenReturn(chelseaVsArsenal);

        Game savedGame = gameServiceImpl.save(chelseaVsArsenal);

        assertAll(() -> {
            assertThat(savedGame.getId(), is(1L));
            assertThat(savedGame.getTeamHome(), is(chelsea));
        });
    }

    @Test
    void shouldDeleteGame_whenGameWithSpecificIdExists() {
        gameServiceImpl.deleteById(1L);
        verify(gameRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteGame_whenGameExists() {
        gameServiceImpl.delete(chelseaVsArsenal);
        verify(gameRepository, times(1)).delete(any());
    }

    @Test
    void shouldSaveAllGames_whenSetWithGameObjectsIsPassed() {
        //given
        Set<Game> gameSet = Sets.newSet(Game.builder().build());
        List<Game> savedGameList = Lists.newArrayList(Game.builder().build());

        doReturn(savedGameList).when(gameRepository).saveAll(gameSet);

        //when
        Set<Game> gamesFromDb = gameServiceImpl.saveAll(gameSet);

        //then
        assertThat(gamesFromDb.size(), is(1));
        verify(gameRepository, times(1)).saveAll(gameSet);
    }

    @Test
    void shouldReturnAllGeneratedGamesForCompetition_whenCompetitionIdIsPassed() {
        //given
        List<Game> foundGameList = Lists.newArrayList(Game.builder().build(), Game.builder().build());

        doReturn(foundGameList).when(gameRepository).findAllGeneratedGames(anyLong());

        //when
        Set<Game> gamesFromDb = gameServiceImpl.findAllGeneratedGamesForCompetition(anyLong());

        //then
        assertThat(gamesFromDb.size(), is(2));
        verify(gameRepository, times(1)).findAllGeneratedGames(anyLong());
    }

    @Test
    void shouldDeleteAllGamesAndLeftSpecificAmount_whenAmountToBeLeftIsPassed() {
        //given
        List<Game> gamesListToBeDeleted = Lists.newArrayList(Game.builder().build(),
                Game.builder().build(),
                Game.builder().build());

        doReturn(gamesListToBeDeleted).when(gameRepository).findAllGamesToBeDeleted(anyInt());

        //when
        gameServiceImpl.deleteOldGames(anyInt());

        //then
        verify(gameRepository, times(1)).findAllGamesToBeDeleted(anyInt());
    }

    @Test
    void shouldGenerateJsonListWithGamesForCompetition_whenCompetitionIdIsPassedAndGameSetSizeIsPositive() {
        //given
        Game game = Game.builder()
                .id(1L)
                .competition(Competition.builder().build())
                .teamAway(Team.builder().name("Chelsea").build())
                .teamHome(Team.builder().name("Arsenal").build())
                .odds(Sets.newSet(
                        Odd.builder().type(GameOddType.HOME_WIN).value(new BigDecimal("1.3")).build(),
                        Odd.builder().type(GameOddType.DRAW).value(new BigDecimal("3.2")).build(),
                        Odd.builder().type(GameOddType.AWAY_WIN).value(new BigDecimal("7.3")).build()
                ))
                .status(GameStatus.PREMATCH)
                .uniqueId("uniqueId")
                .build();

        doReturn(Lists.newArrayList(game))
                .when(gameRepository).findAllGeneratedGames(anyLong());

        //when
        List<JSONObject> generatedGames = gameServiceImpl.generateJsonForInplayGamesForCompetition(anyLong());

        //then
        assertThat(generatedGames.size(), is(2));
        verify(gameRepository, times(1)).findAllGeneratedGames(anyLong());
    }

    @Test
    void shouldNotGenerateJsonListWithGamesForCompetition_whenCompetitionIdIsPassedAndGameSetSizeIsZero() {
        //given
        doReturn(Lists.emptyList()).when(gameRepository).findAllGeneratedGames(anyLong());

        //when
        List<JSONObject> generatedGames = gameServiceImpl.generateJsonForInplayGamesForCompetition(anyLong());

        //then
        assertThat(generatedGames.size(), is(1));
        verify(gameRepository, times(1)).findAllGeneratedGames(anyLong());
    }
}