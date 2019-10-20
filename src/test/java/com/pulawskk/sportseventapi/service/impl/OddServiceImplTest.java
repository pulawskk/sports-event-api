package com.pulawskk.sportseventapi.service.impl;

import com.pulawskk.sportseventapi.entity.Competition;
import com.pulawskk.sportseventapi.entity.Game;
import com.pulawskk.sportseventapi.entity.Odd;
import com.pulawskk.sportseventapi.entity.Team;
import com.pulawskk.sportseventapi.enums.GameOddType;
import com.pulawskk.sportseventapi.repository.OddRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class OddServiceImplTest {

    @Mock
    private OddRepository oddRepository;

    private OddServiceImpl oddServiceImpl;

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
        oddServiceImpl = new OddServiceImpl(oddRepository);

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
    void shouldReturnOdd_WhenOddWithSpecificIdExists() {
        when(oddRepository.findById(anyLong())).thenReturn(Optional.of(chelseaOdd));

        Odd newOdd = oddServiceImpl.findById(1L);

        assertAll(() -> {
            assertThat(newOdd.getId(), is(1L));
            assertThat(newOdd.getType(), is(GameOddType.HOME_WIN));
            assertThat(newOdd.getValue(), is(new BigDecimal("1.5")));
        });
    }

    @Test
    void shouldReturnSetOfOdds_WhenOddsWithSpecificGameExist() {
        when(oddRepository.findAllByGame(any())).thenReturn(new ArrayList<Odd>(odds));

        Set<Odd> oddsFromDb = oddServiceImpl.findAllByGame(chelseaVsArsenal);

        assertAll(() -> {
            assertThat(oddsFromDb, hasSize(3));
            assertThat(oddsFromDb, hasItem(chelseaOdd));
        });
    }

    @Test
    void shouldReturnOdd_whenOddWithSpecificTypeAndGameExists() {
        List<Odd> specificOdds = new ArrayList<>();
        specificOdds.add(chelseaOdd);
        when(oddRepository.findAllByGameAndType(any(), any())).thenReturn(new ArrayList<Odd>(specificOdds));

        Set<Odd> oddsFromDb = oddServiceImpl.findAllByGameAndType(chelseaVsArsenal, GameOddType.HOME_WIN);

        assertThat(oddsFromDb, hasSize(1));
        assertThat(oddsFromDb, contains(chelseaOdd));
    }


    @Test
    void shouldReturnSavedOdd_whenOddIsSaved() {
        when(oddRepository.save(any())).thenReturn(chelseaOdd);

        Odd savedOdd = oddServiceImpl.save(chelseaOdd);

        assertAll(() -> {
            assertThat(savedOdd.getValue(), is(new BigDecimal(1.5)));
            assertThat(savedOdd.getGame(), is(chelseaVsArsenal));
        });
    }

    @Test
    void shouldDeleteOdd_whenOddWithSpecificIdExists() {
        oddServiceImpl.deleteById(1L);
        verify(oddRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void shouldDeleteOdd_whenOddExists() {
        oddServiceImpl.delete(chelseaOdd);
        verify(oddRepository, times(1)).delete(chelseaOdd);
    }

    @Test
    void shouldDeleteOdds_whenBelongToSpecificGame() {
        oddServiceImpl.deleteAllByGame(chelseaVsArsenal);
        verify(oddRepository, times(1)).deleteAllByGame(any());
    }
}